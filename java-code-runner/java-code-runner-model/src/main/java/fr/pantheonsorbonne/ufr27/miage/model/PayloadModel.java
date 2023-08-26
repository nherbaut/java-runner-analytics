package fr.pantheonsorbonne.ufr27.miage.model;

import java.util.ArrayList;
import java.util.Collection;

public class PayloadModel {
    Collection<SourceFile> sources=new ArrayList<>();

    public PayloadModel() {
    }

    public Collection<SourceFile> getSources() {
        return sources;
    }

    public void setSources(Collection<SourceFile> sources) {
        this.sources = sources;
    }

    @Override
    public String toString() {
        return "PayloadModel{" +
                "sources=" + sources +
                '}';
    }
}
