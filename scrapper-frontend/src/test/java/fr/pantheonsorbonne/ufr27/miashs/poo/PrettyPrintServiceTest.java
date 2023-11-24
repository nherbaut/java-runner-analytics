package fr.pantheonsorbonne.ufr27.miashs.poo;

import fr.pantheonsorbonne.ufr27.miashs.poo.business.PrettyPrintService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class PrettyPrintServiceTest {

    @Inject
    PrettyPrintService prettyPrintService;

    @Test
    void prettyHTML() {
        String target = """
<html>
 <head></head>
 <body></body>
</html>""";
        assertEquals(target, prettyPrintService.prettyHTML("<html><body><body></html>"));
    }

    @Test
    void removeScripts() {
        String source = """
<html>
 <head></head>
 <body></body>
 <script>alert("help");</script>
 <script>alert("help");</script>
</html>""";
        String target = """
<html>
 <head></head>
 <body>
 </body>
</html>""";
        assertEquals(target, prettyPrintService.prettyHTML(source));
    }
}