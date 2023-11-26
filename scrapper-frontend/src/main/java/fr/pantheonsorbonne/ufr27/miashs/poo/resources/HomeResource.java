package fr.pantheonsorbonne.ufr27.miashs.poo.resources;

import fr.pantheonsorbonne.ufr27.miashs.poo.business.ClassGenerationService;
import fr.pantheonsorbonne.ufr27.miashs.poo.business.ProjectService;
import fr.pantheonsorbonne.ufr27.miashs.poo.business.SkeletonDownloadService;
import fr.pantheonsorbonne.ufr27.miashs.poo.client.ProjectFactoryResource;
import fr.pantheonsorbonne.ufr27.miashs.poo.model.AssignmentForm;
import fr.pantheonsorbonne.ufr27.miashs.poo.model.FileType;
import fr.pantheonsorbonne.ufr27.miashs.poo.model.InjectedFile;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("/home")
public class HomeResource {

    @Inject
    Template home;


    @Inject
    ProjectService service;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get(@QueryParam("name") String name) {
        return home.data("name", name);
    }

    @Inject
    SkeletonDownloadService skeletonDownloadService;

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Transactional
    public Response processDynamicForm(AssignmentForm assignmentForm) throws IOException, URISyntaxException {


        var is = skeletonDownloadService.getProjectSkeleton(assignmentForm);

        service.bookProject(assignmentForm);
        return Response
                .ok(is, MediaType.APPLICATION_OCTET_STREAM)
                .type("application/zip")
                .header("Content-Disposition", "attachment; filename=projetL2POO.zip")
                .build();

    }


}
