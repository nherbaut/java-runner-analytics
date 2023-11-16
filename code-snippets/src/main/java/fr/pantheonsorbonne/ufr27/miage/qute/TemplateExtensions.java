package fr.pantheonsorbonne.ufr27.miage.qute;


import fr.pantheonsorbonne.ufr27.miage.model.Comment;
import fr.pantheonsorbonne.ufr27.miage.model.File;
import fr.pantheonsorbonne.ufr27.miage.model.Snippet;
import io.quarkus.qute.TemplateExtension;

import java.util.List;

@TemplateExtension
public class TemplateExtensions {

    public static boolean declareNeedHelp(Snippet snippet) {
        return snippet.metas.stream().filter( meta -> meta.key.equals("helpNeeded") && meta.value.equals("true")).findAny().isPresent();
    }
}