package fr.pantheonsorbonne.ufr27.miage.service;

import fr.pantheonsorbonne.ufr27.miage.model.PayloadModel;
import fr.pantheonsorbonne.ufr27.miage.model.Result;

import java.io.IOException;

public interface BuilderAndCompiler {
    Result buildAndCompile(PayloadModel model) throws IOException;
    void reboot();
}
