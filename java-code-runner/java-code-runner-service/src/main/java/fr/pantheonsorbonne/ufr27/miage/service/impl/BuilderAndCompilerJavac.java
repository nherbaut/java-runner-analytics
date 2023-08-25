package fr.pantheonsorbonne.ufr27.miage.service.impl;

import fr.pantheonsorbonne.ufr27.miage.model.MyDiagnostic;
import fr.pantheonsorbonne.ufr27.miage.model.PayloadModel;
import fr.pantheonsorbonne.ufr27.miage.model.Result;
import jakarta.enterprise.inject.Alternative;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Alternative
public class BuilderAndCompilerJavac extends BuilderAndCompilerAdapter {

    private static class StreamGobbler implements Runnable {
        private final InputStream inputStream;
        private final InputStream errorStream;
        private final Consumer<String> stdOutConsumer;
        Consumer<String> stdErrConsumer;

        //stolen from https://www.baeldung.com/run-shell-command-in-java
        public StreamGobbler(InputStream inputStream, InputStream errorStream, Consumer<String> stdOutConsummer, Consumer<String> stdErrConsummer) {
            this.inputStream = inputStream;
            this.errorStream = errorStream;
            this.stdOutConsumer = stdOutConsummer;
            this.stdErrConsumer = stdErrConsummer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines()
                    .forEach(stdOutConsumer);
            new BufferedReader(new InputStreamReader(errorStream)).lines()
                    .forEach(stdErrConsumer);
        }
    }

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();

    @Override
    public Result buildAndCompile(PayloadModel model, long delay, TimeUnit timeUnit) {
        try {
            Result result = new Result();
            Path tmpDir = Files.createTempDirectory("java-runner");


            ProcessBuilder builder = new ProcessBuilder();
            builder.command("sh", "-c", "javac *.java && java Toto");

            builder.directory(tmpDir.toFile());
            Process process = builder.start();

            StreamGobbler streamGobbler =
                    new StreamGobbler(process.getInputStream(), process.getErrorStream(), s -> result.getStdout().add(s), s -> result.getCompilationDiagnostic().add(new MyDiagnostic(s,"")));
            EXECUTOR_SERVICE.submit(streamGobbler);


            process.waitFor();

            return result;


        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }




}
