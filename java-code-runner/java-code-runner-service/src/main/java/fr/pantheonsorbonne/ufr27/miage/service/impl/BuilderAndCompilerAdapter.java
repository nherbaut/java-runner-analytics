package fr.pantheonsorbonne.ufr27.miage.service.impl;

import fr.pantheonsorbonne.ufr27.miage.model.PayloadModel;
import fr.pantheonsorbonne.ufr27.miage.model.Result;
import fr.pantheonsorbonne.ufr27.miage.service.BuilderAndCompiler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BuilderAndCompilerAdapter implements BuilderAndCompiler {

    public List<File> generateFiles(PayloadModel model, Path tmpDir) {
        List<File> compilationUnits = new ArrayList<>();

        model.getSources().forEach(s -> {
            Path source = Path.of(tmpDir.toString(), s.getName());
            try {
                try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(source.toFile()), StandardCharsets.UTF_8))) {
                    writer.write(s.getContent());
                    compilationUnits.add(source.toFile());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);

            }
        });
        return compilationUnits;
    }

    @Override
    public Result buildAndCompile(PayloadModel model, long delay, TimeUnit timeUnit) throws IOException {
        return new Result();
    }

    @Override
    public void reboot() {

    }
}
