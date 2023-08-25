package fr.pantheonsorbonne.ufr27.miage.service;

import fr.pantheonsorbonne.ufr27.miage.model.PayloadModel;
import fr.pantheonsorbonne.ufr27.miage.model.Result;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public interface BuilderAndCompiler {
    Result buildAndCompile(PayloadModel model, long delay, TimeUnit timeUnit) throws IOException;
    void reboot();
}
