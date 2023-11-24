package fr.pantheonsorbonne.ufr27.miashs.poo.business;

import jakarta.enterprise.context.ApplicationScoped;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

@ApplicationScoped
public class PrettyPrintService {

    public String prettyHTML(String content){
        Document doc = Jsoup.parse( content);
        doc.getElementsByTag("script").remove();
        doc.getElementsByTag("style").remove();
        return doc.toString();
    }
}
