package fr.pantheonsorbonne.ufr27.miashs.poo.business;

import com.squareup.javapoet.TypeName;
import fr.pantheonsorbonne.ufr27.miashs.poo.business.ClassGenerationService;
import fr.pantheonsorbonne.ufr27.miashs.poo.business.WebPageSourceCodeService;
import fr.pantheonsorbonne.ufr27.miashs.poo.model.AssignmentForm;
import fr.pantheonsorbonne.ufr27.miashs.poo.model.FormData;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@QuarkusTest
class ClassGenerationServiceTest {




    @Test
    void generateItemScrapping() throws IOException {


        WebPageSourceCodeService mock = Mockito.mock(WebPageSourceCodeService.class);
        Mockito.when(mock.getURLContent(anyString())).thenReturn("noonecare");

        var cgs = new ClassGenerationService(mock);

        AssignmentForm assignmentForm = new AssignmentForm("url",
                List.of(new FormData("prix", Double.class.getCanonicalName(), "item")));
        System.out.println(cgs.generateItem(assignmentForm));
    }

    @Test
    void generateItemsScrapper() throws IOException {
        WebPageSourceCodeService mock = Mockito.mock(WebPageSourceCodeService.class);
        Mockito.when(mock.getURLContent(anyString())).thenReturn("noonecare");

        var cgs = new ClassGenerationService(mock);

        AssignmentForm assignmentForm = new AssignmentForm("url",
                List.of(new FormData("prix", Double.class.getCanonicalName(), "item")));
        System.out.println(cgs.generateItemsScrapper(assignmentForm));
    }

    @Test
    void generatePageContentConstant() throws IOException {
        WebPageSourceCodeService mock = Mockito.mock(WebPageSourceCodeService.class);
        Mockito.when(mock.getURLContent(anyString())).thenReturn("noonecare");

        var cgs = new ClassGenerationService(mock);

        AssignmentForm assignmentForm = new AssignmentForm("url",
                List.of(new FormData("prix", Double.class.getCanonicalName(), "item")));
        System.out.println(cgs.generatePageContentConstant("url"));
    }

    @Test
    void generateItemAnalyzer() throws IOException {
        WebPageSourceCodeService mock = Mockito.mock(WebPageSourceCodeService.class);
        Mockito.when(mock.getURLContent(anyString())).thenReturn("noonecare");

        var cgs = new ClassGenerationService(mock);

        AssignmentForm assignmentForm = new AssignmentForm("url",
                List.of(
                        new FormData("plusGrandprix", Double.class.getCanonicalName(), "items"),
                        new FormData("moyenneDesPrix", Double.class.getCanonicalName(), "items"),
                        new FormData("articleLeMoinsCher", String.class.getCanonicalName(), "items")));
        System.out.println(cgs.generateItemAnalyzer(assignmentForm));
    }

    @Test
    void getType1() throws ClassNotFoundException {
        WebPageSourceCodeService mock = Mockito.mock(WebPageSourceCodeService.class);
        Mockito.when(mock.getURLContent(anyString())).thenReturn("noonecare");

        var cgs = new ClassGenerationService(mock);
        var typeName = cgs.getType(new FormData("toto","java.lang.Integer","item"));
        assertEquals(Integer.class.getCanonicalName(),typeName.toString());


    }

    @Test
    void getType2() throws ClassNotFoundException {
        WebPageSourceCodeService mock = Mockito.mock(WebPageSourceCodeService.class);
        Mockito.when(mock.getURLContent(anyString())).thenReturn("noonecare");

        var cgs = new ClassGenerationService(mock);
        var className = cgs.getType(new FormData("toto","java.util.ArrayList<Integer>","item"));
        assertEquals("java.util.ArrayList<Integer>",className.toString());

    }
}