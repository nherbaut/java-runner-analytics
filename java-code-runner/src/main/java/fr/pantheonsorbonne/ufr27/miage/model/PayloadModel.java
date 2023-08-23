package fr.pantheonsorbonne.ufr27.miage.model;

import fr.pantheonsorbonne.ufr27.miage.model.SourceFile;

import java.util.ArrayList;
import java.util.Collection;

public class PayloadModel {
    Collection<SourceFile> sources=new ArrayList<>();

    public Collection<SourceFile> getSources() {
        return sources;
    }

    public void setSources(Collection<SourceFile> sources) {
        this.sources = sources;
    }
}
