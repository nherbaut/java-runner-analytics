package fr.pantheonsorbonne.ufr27.miashs.poo.resources;

import fr.pantheonsorbonne.ufr27.miashs.poo.business.ClassGenerationService;
import fr.pantheonsorbonne.ufr27.miashs.poo.client.ProjectFactoryResource;
import fr.pantheonsorbonne.ufr27.miashs.poo.model.AssignmentForm;
import fr.pantheonsorbonne.ufr27.miashs.poo.model.FileType;
import fr.pantheonsorbonne.ufr27.miashs.poo.model.InjectedFile;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("/home")
public class HomeResource {

    @Inject
    Template home;


    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get(@QueryParam("name") String name) {
        return home.data("name", name);
    }

    @RestClient
    ProjectFactoryResource projectFactoryResource;

    @Inject
    ClassGenerationService classGenerationService;
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response processDynamicForm(AssignmentForm data) throws IOException {


        String codeItem = classGenerationService.generateItem(data);
        String pageConstant = classGenerationService.generatePageContentConstant(data.assignementURL());
        String cachedPage = classGenerationService.generateCachedPage(data.assignementURL());

        String itemScrapper = classGenerationService.generateItemsScrapper(data);
        String ItemAnalyzer = classGenerationService.generateItemAnalyzer(data);
        AtomicInteger scrappedCount= new AtomicInteger();
        Response resp = projectFactoryResource.post("aHR0cHM6Ly9naXRodWIuY29tL1VGUjI3LzIwMjMtTDItUE9PLXRlbXBsYXRlLmdpdA==",
                Stream.concat(
                Arrays.stream((new String[]{codeItem,pageConstant,itemScrapper,ItemAnalyzer}))
                        .map(s -> new InjectedFile(FileType.SOURCE,s,"")),
                Arrays.stream((new String[]{cachedPage}))
                        .map(s -> new InjectedFile(FileType.ASSET,s,"scrapped"+(scrappedCount.getAndIncrement())+".txt"))).collect(Collectors.toList()));

        var is = resp.readEntity(InputStream.class);
        return Response
                .ok(is, MediaType.APPLICATION_OCTET_STREAM)
                .type("application/zip")
                .header("Content-Disposition", "attachment; filename=projetL2POO.zip")
                .build();
    }


}
