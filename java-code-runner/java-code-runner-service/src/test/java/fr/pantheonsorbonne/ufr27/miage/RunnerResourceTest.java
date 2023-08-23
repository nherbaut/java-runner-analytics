package fr.pantheonsorbonne.ufr27.miage;


import com.fasterxml.jackson.databind.ObjectMapper;
import fr.pantheonsorbonne.ufr27.miage.model.PayloadModel;
import fr.pantheonsorbonne.ufr27.miage.model.Result;
import fr.pantheonsorbonne.ufr27.miage.model.RuntimeError;
import fr.pantheonsorbonne.ufr27.miage.model.SourceFile;
import fr.pantheonsorbonne.ufr27.miage.service.BuilderAndCompiler;

import fr.pantheonsorbonne.ufr27.miage.service.impl.BuilderAndCompilerNative;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;



public class RunnerResourceTest {



    BuilderAndCompiler builderAndCompiler=new BuilderAndCompilerNative();

    @BeforeEach
    public void reboot() {
        builderAndCompiler.reboot();

    }

    @Test
    public void testAllOK(
    ) throws IOException {


        PayloadModel model = new PayloadModel();
        model.getSources().add(new SourceFile("Toto.java", "  public class Toto {\n" +
                "        public static void main(String... args) {\n" +
                "            System.out.println(\"salut\");\n" +
                "        }\n" +
                "    }"));
        var result = new Result();
        result.getStdout().add("salut\n");

        assertEquals(result, builderAndCompiler.buildAndCompile(model));


    }

    @Test

    public void testTimeout() throws IOException {

        PayloadModel model = new PayloadModel();
        model.getSources().add(new SourceFile("Toto.java", "  public class Toto {\n" +
                "        public static void main(String... args) {\n" +
                "            System.out.println(\"salut\");while(true){if(false)break;};\n" +
                "        }\n" +
                "    }"));
        var result = new Result();
        result.getRuntimeError().add(new RuntimeError("Your code timed out", Collections.EMPTY_LIST));
        assertEquals(result, builderAndCompiler.buildAndCompile(model));

    }

    @Test

    public void testCompileError() throws IOException {

        PayloadModel model = new PayloadModel();
        model.getSources().add(new SourceFile("Toto.java", "  public class T oto {\n" +
                "        public static void main(String... args) {\n" +
                "            System.out.println(\"salut\");\n" +
                "        }\n" +
                "    }"));
        ObjectMapper mapper = new ObjectMapper();
        Result result = mapper.readValue("{\"stdout\":[],\"runtimeError\":[],\"compilationDiagnostic\":[{\"source\":\"Toto.java\",\"messageEN\":\"'{' expected\",\"code\":\"compiler.err.expected\",\"position\":16,\"startPosition\":16,\"endPosition\":16,\"lineNumber\":1,\"columnNumber\":17,\"messageFR\":\"'{' expected\",\"kind\":\"ERROR\"}]}", Result.class);
        assertEquals(result, builderAndCompiler.buildAndCompile(model));

    }

}