package fr.pantheonsorbonne.ufr27.miage;


import com.fasterxml.jackson.databind.ObjectMapper;
import fr.pantheonsorbonne.ufr27.miage.model.PayloadModel;
import fr.pantheonsorbonne.ufr27.miage.model.Result;
import fr.pantheonsorbonne.ufr27.miage.model.RuntimeError;
import fr.pantheonsorbonne.ufr27.miage.model.SourceFile;
import fr.pantheonsorbonne.ufr27.miage.service.BuilderAndCompiler;
import fr.pantheonsorbonne.ufr27.miage.service.impl.BuilderAndCompilerNative;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class RunnerResourceTest {

    @Test
    public void testBlackList() throws IOException {
        String code = "import java.util.ArrayList;\n" +
                "import java.util.Collection;\n" +
                "import java.util.function.Function;\n" +
                "import java.util.function.UnaryOperator;\n" +
                "\n" +
                "public class Main {\n" +
                "\n" +
                "    \n" +
                "    public static void main(String... args) {\n" +
                "\n" +
                "    UnaryOperator<Character> lowerCase = c -> Character.toLowerCase(c);\n" +
                "\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "}\n";

        BuilderAndCompiler builderAndCompiler = new BuilderAndCompilerNative();
        PayloadModel model = new PayloadModel();
        model.getSources().add(new SourceFile("Main.java", code));
        var result = new Result();
        result.appendStdout("");

        assertEquals(result, builderAndCompiler.buildAndCompile(model, 3, TimeUnit.SECONDS));
    }

    @Test
    public void testAllOK(
    ) throws IOException {

        BuilderAndCompiler builderAndCompiler = new BuilderAndCompilerNative();
        PayloadModel model = new PayloadModel();
        model.getSources().add(new SourceFile("Toto.java", "  public class Toto {\n" +
                "        public static void main(String... args) {\n" +
                "            System.out.println(\"salut\");\n" +
                "        }\n" +
                "    }"));
        var result = new Result();
        result.appendStdout("salut\n");

        assertEquals(result, builderAndCompiler.buildAndCompile(model, 3, TimeUnit.SECONDS));


    }

    @Test
    public void testVoidCode() throws IOException {
        BuilderAndCompiler builderAndCompiler = new BuilderAndCompilerNative();
        PayloadModel model = new PayloadModel();
        model.getSources().add(new SourceFile("Toto.java", ""));
        Result res = builderAndCompiler.buildAndCompile(model, 3, TimeUnit.SECONDS);
        assertEquals(1,res.getRuntimeError().size());

    }

    @Test

    public void testTimeout() throws IOException {
        BuilderAndCompiler builderAndCompiler = new BuilderAndCompilerNative();
        PayloadModel model = new PayloadModel();
        model.getSources().add(new SourceFile("Toto.java", "  public class Toto {\n" +
                "        public static void main(String... args) {\n" +
                "            System.out.println(\"salut\");while(true){if(false)break;};\n" +
                "        }\n" +
                "    }"));
        var expected = new Result();
        expected.getRuntimeError().add(new RuntimeError("Your code timed out", Collections.EMPTY_LIST));
        var result = builderAndCompiler.buildAndCompile(model, 5, TimeUnit.SECONDS);
        assertEquals(expected.getRuntimeError(), result.getRuntimeError());
        assertEquals(expected.getCompilationDiagnostic(), result.getCompilationDiagnostic());

    }

    @Test

    public void testCompileError() throws IOException {
        BuilderAndCompiler builderAndCompiler = new BuilderAndCompilerNative();
        PayloadModel model = new PayloadModel();
        model.getSources().add(new SourceFile("Toto.java", "  public class T oto {\n" +
                "        public static void main(String... args) {\n" +
                "            System.out.println(\"salut\");\n" +
                "        }\n" +
                "    }"));
        ObjectMapper mapper = new ObjectMapper();
        Result expected = mapper.readValue("{\"stdout\":\"\",\"runtimeError\":[],\"compilationDiagnostic\":[{\"source\":\"Toto.java\",\"messageEN\":\"'{' expected\",\"code\":\"compiler.err.expected\",\"position\":16,\"startPosition\":16,\"endPosition\":16,\"lineNumber\":1,\"columnNumber\":17,\"messageFR\":\"'{' expected\",\"kind\":\"ERROR\"}]}", Result.class);
        var result = builderAndCompiler.buildAndCompile(model, 10, TimeUnit.SECONDS);
        assertEquals(expected, result);

    }

    @Test
    public void testForbiddenClass() throws IOException {
        BuilderAndCompiler builderAndCompiler = new BuilderAndCompilerNative();
        PayloadModel model = new PayloadModel();
        model.getSources().add(new SourceFile("FileWritingTest.java", "import java.io.BufferedWriter;\n" +
                "import java.io.FileWriter;\n" +
                "import java.io.IOException;\n" +
                "\n" +
                "public class FileWritingTest {\n" +
                "    public static void main(String[] args) {\n" +
                "        String directoryPath = \"/home/app\";  // Update this with the desired directory path\n" +
                "        String fileName = \"testFile.txt\";\n" +
                "        String fileContent = \"This is the content of the test file.\";\n" +
                "\n" +
                "        try {\n" +
                "            String filePath = directoryPath + \"/\" + fileName;\n" +
                "            \n" +
                "            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));\n" +
                "            writer.write(fileContent);\n" +
                "            writer.close();\n" +
                "            \n" +
                "            System.out.println(\"File created and written successfully: \" + filePath);\n" +
                "        } catch (IOException e) {\n" +
                "            System.err.println(\"An error occurred: \" + e.getMessage());\n" +
                "        }\n" +
                "    }\n" +
                "}\n"));
        ObjectMapper mapper = new ObjectMapper();
        Result result = builderAndCompiler.buildAndCompile(model, 10, TimeUnit.SECONDS);
        System.out.println(result.toString());
        assertTrue(result.getStdout().isEmpty());
        assertTrue(result.getCompilationDiagnostic().isEmpty());
        assertTrue(result.getRuntimeError().size() == 1);
    }

}