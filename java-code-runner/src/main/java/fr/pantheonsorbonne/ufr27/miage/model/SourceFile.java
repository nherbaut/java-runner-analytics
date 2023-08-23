package fr.pantheonsorbonne.ufr27.miage.model;

public class SourceFile {
    private String name;
    private String content;

    public SourceFile(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public SourceFile() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
