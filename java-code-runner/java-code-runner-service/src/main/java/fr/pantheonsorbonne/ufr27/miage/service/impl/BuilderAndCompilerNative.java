package fr.pantheonsorbonne.ufr27.miage.service.impl;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.google.common.collect.Lists;
import fr.pantheonsorbonne.ufr27.miage.model.*;
import jakarta.enterprise.context.ApplicationScoped;

import javax.tools.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;

@ApplicationScoped
public class BuilderAndCompilerNative extends BuilderAndCompilerAdapter {

    private ExecutorService executorService = Executors.newSingleThreadExecutor();


    @Override
    public Result buildAndCompile(PayloadModel model) throws IOException {
        Result res = new Result();
        Path tmpDir = Files.createTempDirectory("java-runner");
        try {
            List<File> files = generateFiles(model, tmpDir);

            MethodDeclaration methodDeclaration = null;
            try {
                methodDeclaration = findAMainMethod(model);
            } catch (ParseProblemException e) {
                //can't parse the Javafile, it means we let the compiler report the errors
            }

            //System.out.println(String.format("I will run the method %s of class %s", methodDeclaration.getNameAsString(), methodDeclaration.findAncestor(TypeDeclaration.class).get().getName()));


            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
            StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, Locale.FRENCH, StandardCharsets.UTF_8);
            fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Collections.singleton(tmpDir.toFile()));

            Iterable<? extends JavaFileObject> compilationUnits1 =
                    fileManager.getJavaFileObjectsFromFiles(files);
            JavaCompiler.CompilationTask compilationTask = compiler.getTask(null, fileManager, diagnostics, Lists.newArrayList("-g"), null, compilationUnits1);
            if (compilationTask.call()) {

                ClassLoader urlClassLoader = new URLClassLoader("java-runner", new URL[]{tmpDir.toUri().toURL()}, this.getClass().getClassLoader());

                try {


                    Method method = getMainMethodFromMethodDeclaration(methodDeclaration, urlClassLoader);
                    Future<?> execution = executorService.submit(() -> {
                        try {
                            invokeMethod(res, method);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    });

                    try {
                        execution.get(10, TimeUnit.SECONDS);
                    } catch (ExecutionException | InterruptedException | TimeoutException ie) {
                        res.getRuntimeError().add(new RuntimeError("Your code timed out"));
                        execution.cancel(true);
                    }


                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }


            }


            diagnostics.getDiagnostics().

                    forEach((diagnostic) ->

                            res.getCompilationDiagnostic().add(MyDiagnostic.fromDiagnosis(diagnostic)));


            fileManager.close();
            return res;
        }finally {
            //stolen from https://www.baeldung.com/java-delete-directory
            Files.walk(tmpDir)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }

    @Override
    public void reboot() {
        executorService.shutdownNow();
        executorService = Executors.newSingleThreadExecutor();
    }

    private static MethodDeclaration findAMainMethod(PayloadModel model) {
        return model.getSources().stream()
                .map(s -> StaticJavaParser.parse(s.getContent()))
                .flatMap(cu -> cu.getTypes().stream())
                .flatMap(type -> type.getMethods().stream())
                .filter(md -> Objects.equals("main", md.getNameAsString()))
                .filter(md -> md.getModifiers().contains(com.github.javaparser.ast.Modifier.staticModifier()))
                .filter(md -> md.getModifiers().contains(com.github.javaparser.ast.Modifier.publicModifier()))
                .filter(md -> md.getType().asString().equals("void"))
                .filter(md -> md.getParameters().getFirst().isPresent() &&
                        (md.getParameters().getFirst().get().isVarArgs() && md.getParameters().getFirst().get().getType().asString().equals("String"))
                        || (md.getParameters().getFirst().get().getType().isArrayType()) && md.getParameters().getFirst().get().getType().asArrayType().getElementType().asString().equals("String"))
                .findFirst().orElseThrow();
    }

    private static Method getMainMethodFromMethodDeclaration(MethodDeclaration methodDeclaration, ClassLoader urlClassLoader) throws ClassNotFoundException {
        return Arrays.stream(urlClassLoader.loadClass(methodDeclaration.findAncestor(TypeDeclaration.class).get().getNameAsString()).getMethods())
                .filter(m -> m.getName().equals(methodDeclaration.getNameAsString()))
                .filter(m -> (m.getModifiers() & (Modifier.PUBLIC + Modifier.STATIC)) == (Modifier.PUBLIC + Modifier.STATIC))
                .filter(m -> m.getReturnType().getName().equals("void"))
                .filter(m -> Arrays.stream(m.getParameterTypes())
                        .anyMatch(pt -> pt.toString().equals("class [Ljava.lang.String;")))
                .findAny().orElseThrow();
    }

    private static void invokeMethod(Result res, Method method) throws IllegalAccessException, InvocationTargetException {
        ByteArrayOutputStream out = null;
        ByteArrayOutputStream err = null;
        PrintStream out1 = null;
        PrintStream err1 = null;
        try {
            out = new ByteArrayOutputStream();
            err = new ByteArrayOutputStream();
            out1 = new PrintStream(out);
            System.setOut(out1);
            err1 = new PrintStream(err);
            System.setErr(err1);
            try {
                method.invoke(null, new Object[]{new String[0]});
            } catch (Throwable t) {
                var runTimeError = new RuntimeError(t.getCause().getMessage());
                res.getRuntimeError().add(runTimeError);


                Arrays.stream(t.getCause().getStackTrace()).filter(ste -> "java-runner".equals(ste.getClassLoaderName())).forEach(ste -> runTimeError.getStackTraceElements().add(new MinimalStackTraceElement(ste.getClassName(), ste.getMethodName(), ste.getLineNumber())));
            }
            out1.flush();
            err1.flush();
            res.getStdout().add(out.toString());
        } finally {
            closeIfNotNull(out, err, out1, err1);

        }

    }

    private static void closeIfNotNull(Closeable... closeables) {
        Arrays.stream(closeables).forEach(c -> {
            if (c != null) {
                try {
                    c.close();
                } catch (IOException e) {
                    // fall through
                }
            }
        });
    }

}
