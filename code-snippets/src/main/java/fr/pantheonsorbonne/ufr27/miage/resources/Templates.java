package fr.pantheonsorbonne.ufr27.miage.resources;

import fr.pantheonsorbonne.ufr27.miage.model.Snippet;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;

import java.util.List;

@CheckedTemplate
public class Templates {
    public static native TemplateInstance index(List<Snippet> snippets, String codeSnippetApiURL);
}
