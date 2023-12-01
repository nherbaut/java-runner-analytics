package fr.pantheonsorbonne.ufr27.miashs.poo.resources;

import fr.pantheonsorbonne.ufr27.miashs.poo.business.SkeletonDownloadService;
import fr.pantheonsorbonne.ufr27.miashs.poo.dao.ProjetDAO;
import fr.pantheonsorbonne.ufr27.miashs.poo.entities.Projet;
import fr.pantheonsorbonne.ufr27.miashs.poo.model.AssignmentForm;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Path("/projects")
public class ProjetResource {

    @Inject
    Template projets;

    @Inject
    ProjetDAO dao;

    @Inject
    SkeletonDownloadService skeletonDownloadService;

    @Path("/{projectID}")
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getProject(@PathParam("projectID") UUID projectID) {
        try {
            AssignmentForm assignmentForm = dao.getAssignementFormByProjectId(projectID);
            var is = skeletonDownloadService.getProjectSkeleton(assignmentForm);

                return Response
                        .ok(is, MediaType.APPLICATION_OCTET_STREAM)
                        .type("application/zip")
                        .header("Content-Disposition", "attachment; filename=projetL2POO.zip")
                        .build();

        } catch (Throwable e) {
            throw new WebApplicationException("impossible de télécharger le projet", 404);
        }
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get(@QueryParam("name") String name) {

        List<Projet> projetList = dao.getAllProjects();
        projetList.sort((o1, o2) -> {
            int web = o1.getWebsite().compareTo(o2.getWebsite());
            if (web == 0) {
                return o1.getCreationTime().compareTo(o2.getCreationTime());
            } else {
                return web;
            }
        });

        return projets.data("projects", projetList.stream().collect(Collectors.groupingBy(Projet::getWebsite)));
    }

}
