package fr.pantheonsorbonne.ufr27.miage.service.impl;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.google.common.collect.Lists;
import fr.pantheonsorbonne.ufr27.miage.model.*;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.tools.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@ApplicationScoped
public class BuilderAndCompilerNative extends BuilderAndCompilerAdapter {

    private static final Logger LOGGER = LogManager.getLogger(BuilderAndCompilerNative.class);

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final static Collection<Pattern> forbiddenPatterns;

    static {
        final Collection<String> forbiddenClasses = new ArrayList<>();
        forbiddenClasses.add("com.sun.jarsigner.*");
        forbiddenClasses.add("com.sun.java.accessibility.util.*");
        forbiddenClasses.add("com.sun.jdi.*");
        forbiddenClasses.add("com.sun.jdi.connect.*");
        forbiddenClasses.add("com.sun.jdi.connect.spi.*");
        forbiddenClasses.add("com.sun.jdi.event.*");
        forbiddenClasses.add("com.sun.jdi.request.*");
        forbiddenClasses.add("com.sun.management.*");
        forbiddenClasses.add("com.sun.net.httpserver.*");
        forbiddenClasses.add("com.sun.net.httpserver.spi.*");
        forbiddenClasses.add("com.sun.nio.sctp.*");
        forbiddenClasses.add("com.sun.security.auth.*");
        forbiddenClasses.add("com.sun.security.auth.callback.*");
        forbiddenClasses.add("com.sun.security.auth.login.*");
        forbiddenClasses.add("com.sun.security.auth.module.*");
        forbiddenClasses.add("com.sun.security.jgss.*");
        forbiddenClasses.add("com.sun.source.doctree.*");
        forbiddenClasses.add("com.sun.source.tree.*");
        forbiddenClasses.add("com.sun.source.util.*");
        forbiddenClasses.add("com.sun.tools.attach.*");
        forbiddenClasses.add("com.sun.tools.attach.spi.*");
        forbiddenClasses.add("com.sun.tools.javac.*");
        forbiddenClasses.add("com.sun.tools.jconsole.*");
        forbiddenClasses.add("java.applet.*");
        forbiddenClasses.add("java.awt.*");
        forbiddenClasses.add("java.awt.color.*");
        forbiddenClasses.add("java.awt.datatransfer.*");
        forbiddenClasses.add("java.awt.desktop.*");
        forbiddenClasses.add("java.awt.dnd.*");
        forbiddenClasses.add("java.awt.event.*");
        forbiddenClasses.add("java.awt.font.*");
        forbiddenClasses.add("java.awt.geom.*");
        forbiddenClasses.add("java.awt.im.*");
        forbiddenClasses.add("java.awt.im.spi.*");
        forbiddenClasses.add("java.awt.image.*");
        forbiddenClasses.add("java.awt.image.renderable.*");
        forbiddenClasses.add("java.awt.print.*");
        forbiddenClasses.add("java.beans.*");
        forbiddenClasses.add("java.beans.beancontext.*");
// forbiddenClasses.add("java.io.*");
// forbiddenClasses.add("java.lang.*");
// forbiddenClasses.add("java.lang.annotation.*");
// forbiddenClasses.add("java.lang.constant.*");
// forbiddenClasses.add("java.lang.instrument.*");
// forbiddenClasses.add("java.lang.invoke.*");
// forbiddenClasses.add("java.lang.management.*");
// forbiddenClasses.add("java.lang.module.*");
// forbiddenClasses.add("java.lang.ref.*");
// forbiddenClasses.add("java.lang.reflect.*");
// forbiddenClasses.add("java.lang.runtime.*");
// forbiddenClasses.add("java.math.*");
        forbiddenClasses.add("java.net.*");
        forbiddenClasses.add("java.net.http.*");
        forbiddenClasses.add("java.net.spi.*");
        forbiddenClasses.add("java.nio.*");
        forbiddenClasses.add("java.nio.channels.*");
        forbiddenClasses.add("java.nio.channels.spi.*");
        forbiddenClasses.add("java.nio.charset.*");
        forbiddenClasses.add("java.nio.charset.spi.*");
        forbiddenClasses.add("java.nio.file.*");
        forbiddenClasses.add("java.nio.file.attribute.*");
        forbiddenClasses.add("java.nio.file.spi.*");
        forbiddenClasses.add("java.rmi.*");
        forbiddenClasses.add("java.rmi.dgc.*");
        forbiddenClasses.add("java.rmi.registry.*");
        forbiddenClasses.add("java.rmi.server.*");
        forbiddenClasses.add("java.security.*");
        forbiddenClasses.add("java.security.cert.*");
        forbiddenClasses.add("java.security.interfaces.*");
        forbiddenClasses.add("java.security.spec.*");
        forbiddenClasses.add("java.sql.*");
        forbiddenClasses.add("java.text.*");
        forbiddenClasses.add("java.text.spi.*");
// forbiddenClasses.add("java.time.*");
        forbiddenClasses.add("java.time.chrono.*");
        forbiddenClasses.add("java.time.format.*");
        forbiddenClasses.add("java.time.temporal.*");
        forbiddenClasses.add("java.time.zone.*");
// forbiddenClasses.add("java.util.*");
// forbiddenClasses.add("java.util.concurrent.*");
// forbiddenClasses.add("java.util.concurrent.atomic.*");
// forbiddenClasses.add("java.util.concurrent.locks.*");
// forbiddenClasses.add("java.util.function.*");
// forbiddenClasses.add("java.util.jar.*");
// forbiddenClasses.add("java.util.logging.*");
// forbiddenClasses.add("java.util.prefs.*");
// forbiddenClasses.add("java.util.random.*");
// forbiddenClasses.add("java.util.regex.*");
// forbiddenClasses.add("java.util.spi.*");
// forbiddenClasses.add("java.util.stream.*");
        forbiddenClasses.add("java.util.zip.*");
        forbiddenClasses.add("javax.accessibility.*");
// forbiddenClasses.add("javax.annotation.processing.*");
// forbiddenClasses.add("javax.crypto.*");
// forbiddenClasses.add("javax.crypto.interfaces.*");
// forbiddenClasses.add("javax.crypto.spec.*");
        forbiddenClasses.add("javax.imageio.*");
        forbiddenClasses.add("javax.imageio.event.*");
        forbiddenClasses.add("javax.imageio.metadata.*");
        forbiddenClasses.add("javax.imageio.plugins.bmp.*");
        forbiddenClasses.add("javax.imageio.plugins.jpeg.*");
        forbiddenClasses.add("javax.imageio.plugins.tiff.*");
        forbiddenClasses.add("javax.imageio.spi.*");
        forbiddenClasses.add("javax.imageio.stream.*");
// forbiddenClasses.add("javax.lang.model.*");
// forbiddenClasses.add("javax.lang.model.element.*");
// forbiddenClasses.add("javax.lang.model.type.*");
// forbiddenClasses.add("javax.lang.model.util.*");
        forbiddenClasses.add("javax.management.*");
        forbiddenClasses.add("javax.management.loading.*");
        forbiddenClasses.add("javax.management.modelmbean.*");
        forbiddenClasses.add("javax.management.monitor.*");
        forbiddenClasses.add("javax.management.openmbean.*");
        forbiddenClasses.add("javax.management.relation.*");
        forbiddenClasses.add("javax.management.remote.*");
        forbiddenClasses.add("javax.management.remote.rmi.*");
        forbiddenClasses.add("javax.management.timer.*");
        forbiddenClasses.add("javax.naming.*");
        forbiddenClasses.add("javax.naming.directory.*");
        forbiddenClasses.add("javax.naming.event.*");
        forbiddenClasses.add("javax.naming.ldap.*");
        forbiddenClasses.add("javax.naming.ldap.spi.*");
        forbiddenClasses.add("javax.naming.spi.*");
        forbiddenClasses.add("javax.net.*");
        forbiddenClasses.add("javax.net.ssl.*");
        forbiddenClasses.add("javax.print.*");
        forbiddenClasses.add("javax.print.attribute.*");
        forbiddenClasses.add("javax.print.attribute.standard.*");
        forbiddenClasses.add("javax.print.event.*");
        forbiddenClasses.add("javax.rmi.ssl.*");
        forbiddenClasses.add("javax.script.*");
        forbiddenClasses.add("javax.security.auth.*");
        forbiddenClasses.add("javax.security.auth.callback.*");
        forbiddenClasses.add("javax.security.auth.kerberos.*");
        forbiddenClasses.add("javax.security.auth.login.*");
        forbiddenClasses.add("javax.security.auth.spi.*");
        forbiddenClasses.add("javax.security.auth.x500.*");
        forbiddenClasses.add("javax.security.cert.*");
        forbiddenClasses.add("javax.security.sasl.*");
        forbiddenClasses.add("javax.smartcardio.*");
        forbiddenClasses.add("javax.sound.midi.*");
        forbiddenClasses.add("javax.sound.midi.spi.*");
        forbiddenClasses.add("javax.sound.sampled.*");
        forbiddenClasses.add("javax.sound.sampled.spi.*");
        forbiddenClasses.add("javax.sql.*");
        forbiddenClasses.add("javax.sql.rowset.*");
        forbiddenClasses.add("javax.sql.rowset.serial.*");
        forbiddenClasses.add("javax.sql.rowset.spi.*");
        forbiddenClasses.add("javax.swing.*");
        forbiddenClasses.add("javax.swing.border.*");
        forbiddenClasses.add("javax.swing.colorchooser.*");
        forbiddenClasses.add("javax.swing.event.*");
        forbiddenClasses.add("javax.swing.filechooser.*");
        forbiddenClasses.add("javax.swing.plaf.*");
        forbiddenClasses.add("javax.swing.plaf.basic.*");
        forbiddenClasses.add("javax.swing.plaf.metal.*");
        forbiddenClasses.add("javax.swing.plaf.multi.*");
        forbiddenClasses.add("javax.swing.plaf.nimbus.*");
        forbiddenClasses.add("javax.swing.plaf.synth.*");
        forbiddenClasses.add("javax.swing.table.*");
        forbiddenClasses.add("javax.swing.text.*");
        forbiddenClasses.add("javax.swing.text.html.*");
        forbiddenClasses.add("javax.swing.text.html.parser.*");
        forbiddenClasses.add("javax.swing.text.rtf.*");
        forbiddenClasses.add("javax.swing.tree.*");
        forbiddenClasses.add("javax.swing.undo.*");
        forbiddenClasses.add("javax.tools.*");
        forbiddenClasses.add("javax.transaction.xa.*");
// forbiddenClasses.add("javax.xml.*");
// forbiddenClasses.add("javax.xml.catalog.*");
// forbiddenClasses.add("javax.xml.crypto.*");
// forbiddenClasses.add("javax.xml.crypto.dom.*");
// forbiddenClasses.add("javax.xml.crypto.dsig.*");
// forbiddenClasses.add("javax.xml.crypto.dsig.dom.*");
// forbiddenClasses.add("javax.xml.crypto.dsig.keyinfo.*");
// forbiddenClasses.add("javax.xml.crypto.dsig.spec.*");
// forbiddenClasses.add("javax.xml.datatype.*");
// forbiddenClasses.add("javax.xml.namespace.*");
// forbiddenClasses.add("javax.xml.parsers.*");
// forbiddenClasses.add("javax.xml.stream.*");
// forbiddenClasses.add("javax.xml.stream.events.*");
// forbiddenClasses.add("javax.xml.stream.util.*");
// forbiddenClasses.add("javax.xml.transform.*");
// forbiddenClasses.add("javax.xml.transform.dom.*");
// forbiddenClasses.add("javax.xml.transform.sax.*");
// forbiddenClasses.add("javax.xml.transform.stax.*");
// forbiddenClasses.add("javax.xml.transform.stream.*");
// forbiddenClasses.add("javax.xml.validation.*");
// forbiddenClasses.add("javax.xml.xpath.*");
        forbiddenClasses.add("jdk.dynalink.*");
        forbiddenClasses.add("jdk.dynalink.beans.*");
        forbiddenClasses.add("jdk.dynalink.linker.*");
        forbiddenClasses.add("jdk.dynalink.linker.support.*");
        forbiddenClasses.add("jdk.dynalink.support.*");
        forbiddenClasses.add("jdk.incubator.foreign.*");
        forbiddenClasses.add("jdk.incubator.vector.*");
        forbiddenClasses.add("jdk.javadoc.doclet.*");
        forbiddenClasses.add("jdk.jfr.*");
        forbiddenClasses.add("jdk.jfr.consumer.*");
        forbiddenClasses.add("jdk.jshell.*");
        forbiddenClasses.add("jdk.jshell.execution.*");
        forbiddenClasses.add("jdk.jshell.spi.*");
        forbiddenClasses.add("jdk.jshell.tool.*");
        forbiddenClasses.add("jdk.management.jfr.*");
        forbiddenClasses.add("jdk.net.*");
        forbiddenClasses.add("jdk.nio.*");
        forbiddenClasses.add("jdk.nio.mapmode.*");
        forbiddenClasses.add("jdk.security.jarsigner.*");
        forbiddenClasses.add("netscape.javascript.*");
// forbiddenClasses.add("org.ietf.jgss.*");
// forbiddenClasses.add("org.w3c.dom.*");
// forbiddenClasses.add("org.w3c.dom.bootstrap.*");
// forbiddenClasses.add("org.w3c.dom.css.*");
// forbiddenClasses.add("org.w3c.dom.events.*");
// forbiddenClasses.add("org.w3c.dom.html.*");
// forbiddenClasses.add("org.w3c.dom.ls.*");
// forbiddenClasses.add("org.w3c.dom.ranges.*");
// forbiddenClasses.add("org.w3c.dom.stylesheets.*");
// forbiddenClasses.add("org.w3c.dom.traversal.*");
// forbiddenClasses.add("org.w3c.dom.views.*");
// forbiddenClasses.add("org.w3c.dom.xpath.*");
// forbiddenClasses.add("org.xml.sax.*");
// forbiddenClasses.add("org.xml.sax.ext.*");
// forbiddenClasses.add("org.xml.sax.helpers .*");

        forbiddenClasses.add("java.io.FileWriter");
        forbiddenClasses.add("java.io.FileOutputStream");
        forbiddenClasses.add("java.io.FileReader");
        forbiddenClasses.add("java.io.FileInputStream");
        forbiddenClasses.add("java.nio.file.*");
        forbiddenClasses.add("java.lang.Thread");

        forbiddenClasses.add("java.lang.instrument.*");
        forbiddenClasses.add("java.lang.invoke.*");

        forbiddenClasses.add("java.lang.ref.*");

        forbiddenClasses.add("java.lang.reflect.*");
        forbiddenClasses.add("java.lang.runtime.*");
        forbiddenClasses.add("java.lang.module.*");
        forbiddenClasses.add("java.lang.management.*");
        forbiddenClasses.add("sun.misc.Unsafe");



        forbiddenPatterns = forbiddenClasses.stream().map(s -> Pattern.compile(s)).collect(Collectors.toList());
    }

    @Override
    public Result buildAndCompile(PayloadModel model, long delay, TimeUnit timeUnit) throws IOException {
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


                MethodDeclaration finalMethodDeclaration = methodDeclaration;
                Future<?> execution = executorService.submit(() -> {
                    try {
                        ClassLoader urlClassLoader = new FilteringURLClassLoader(tmpDir);
                        Method method = getMainMethodFromMethodDeclaration(finalMethodDeclaration, urlClassLoader);
                        invokeMethod(res, method);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                });

                try {
                    execution.get(delay, timeUnit);
                } catch (TimeoutException ie) {
                    res.getRuntimeError().add(new RuntimeError("Your code timed out"));


                } catch (ForbiddenClassException | ExecutionException | InterruptedException e) {
                    putErrorInTheResult(res, e);
                } finally {
                    execution.cancel(true);
                }


            }


            diagnostics.getDiagnostics().

                    forEach((diagnostic) ->

                            res.getCompilationDiagnostic().add(MyDiagnostic.fromDiagnosis(diagnostic)));


            fileManager.close();
            return res;
        } finally {
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
                //if an error happens in the code, it will be wrapped in a cause
                //if an error happens with our stuff, there will be no cause, so pass the throwable directlymake
                putErrorInTheResult(res, t.getCause() != null ? t.getCause() : t);
            }
            out1.flush();
            err1.flush();
            res.getStdout().add(out.toString());
        } finally {
            closeIfNotNull(out, err, out1, err1);

        }

    }

    private static void putErrorInTheResult(Result res, Throwable t) {
        //get message is sometime lacking, replacing by toString
        var runTimeError = new RuntimeError(t.toString());
        res.getRuntimeError().add(runTimeError);


        Arrays.stream(t.getStackTrace()).filter(ste -> "java-runner".equals(ste.getClassLoaderName())).forEach(ste -> runTimeError.getStackTraceElements().add(new MinimalStackTraceElement(ste.getClassName(), ste.getMethodName(), ste.getLineNumber())));
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

    private class FilteringURLClassLoader extends URLClassLoader {
        public FilteringURLClassLoader(Path tmpDir) throws MalformedURLException {
            super("java-runner", new URL[]{tmpDir.toUri().toURL()}, BuilderAndCompilerNative.this.getClass().getClassLoader());
        }

        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            LOGGER.info("loaded class" + name);

            if (forbiddenPatterns.stream()
                    .map(p -> p.matcher(name).matches())
                    .filter(b -> b.booleanValue())
                    .findAny()
                    .isPresent()) {
                LOGGER.warn("class {} matched the blacklist", name);
                throw new ForbiddenClassException("class " + name + " matched the blacklist");
            }
            return super.loadClass(name);

        }


    }
}
