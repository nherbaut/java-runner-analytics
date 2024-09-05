package fr.pantheonsorbonne.ufr27.miage.resources;


import fr.pantheonsorbonne.ufr27.miage.model.Snippet;
import fr.pantheonsorbonne.ufr27.miage.service.SnippetService;
import io.quarkus.panache.common.Sort;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Path("snippet")
public class CodeSnippetResource {
    @ConfigProperty(name = "fr.pantheonsorbonne.miage.codeSnippetAPIURL")
    String codeSnippetApiURL;

    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @PermitAll
    public Collection<Snippet> getAllSnippetsPaged(@DefaultValue("0") @QueryParam("pageIndex") int pageIndex, @DefaultValue("25") @QueryParam("pageSize") int pageSize) {
        return Snippet.findAll(Sort.descending("owner").and("lastTouchedTime")).page(pageIndex, pageSize).list();

    }

    @Path("all")
    @Produces(MediaType.TEXT_HTML)
    @GET
    @PermitAll
    public TemplateInstance getAllSnippetsPagedHTML(@DefaultValue("0") @QueryParam("pageIndex") int pageIndex, @DefaultValue("1000") @QueryParam("pageSize") int pageSize) {
        return fr.pantheonsorbonne.ufr27.miage.resources.Templates.index(Snippet.findAll().page(pageIndex, pageSize).list(), codeSnippetApiURL);

    }


    @Path("mine")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Authenticated
    public Collection<Snippet> getMySnippets(@Context SecurityContext context, @DefaultValue("0") @QueryParam("pageIndex") int pageIndex, @DefaultValue("25") @QueryParam("pageSize") int pageSize) {

        return Snippet.find("SELECT s from Snippet s WHERE s.owner=?1 ORDER by s.lastTouchedTime DESC", context.getUserPrincipal().getName()).page(pageIndex, pageSize).list();

    }


    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance showSnippet(Snippet snippet, String codeSnippetApiURL, boolean canEdit);
    }


    @Path("{snippetId}")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @PermitAll
    public Snippet getSnippet(@PathParam("snippetId") String snippetId) {
        Snippet res = Snippet.findById(snippetId);
        if (res == null) {
            throw new WebApplicationException(404);
        } else {
            return res;
        }
    }

    @Inject
    SnippetService snippetService;

    @Path("search")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @PermitAll
    public Collection<Snippet> searchSnippet(@QueryParam("query") String query) {
        query = Objects.requireNonNullElse(query, "");
        Map<String, String> metaSearch = Arrays.stream(query.split(",")).collect(Collectors.toMap(s -> s.split(":")[0], s -> s.split(":")[1]));
        return snippetService.searchSnippets(metaSearch);

    }

    @Path("{snippetId}")
    @Produces(MediaType.TEXT_HTML)
    @GET
    @PermitAll
    public TemplateInstance getSnippetHTML(@PathParam("snippetId") String snippetId, @Context SecurityContext securityContext) {
        boolean canEdit = false;
        Snippet snippet = this.getSnippet(snippetId);
        if (securityContext.getUserPrincipal() != null && (snippet.owner.equals(securityContext.getUserPrincipal().getName()) || securityContext.isUserInRole("helpers"))) {
            canEdit = true;
        }

        return Templates.showSnippet(snippet, codeSnippetApiURL, canEdit);
    }


    @Transactional
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Authenticated
    public Response postSnippet(Snippet snippet, @Context SecurityContext ctx) {
        snippet.owner = ctx.getUserPrincipal().getName();
        snippet.lastTouchedTime = Instant.now();
        Snippet.persist(snippet);

        return Response.created(UriBuilder.fromUri(codeSnippetApiURL).path("snippet").path(snippet.id).build()).build();
    }


    @Transactional
    @PUT
    @Authenticated
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("{snippetId}")
    public Response updateSnippet(@PathParam("snippetId") String snippetId, Snippet snippet, @Context SecurityContext ctx) {
        Snippet snippetDB = Snippet.findById(snippetId);
        if (snippetDB == null) {
            throw new WebApplicationException(404);
        } else {
            if (Objects.equals(snippetDB.owner, ctx.getUserPrincipal().getName()) || ctx.isUserInRole("helpers")) {
                snippetDB.title = snippet.title != null ? snippet.title : snippetDB.title;
                snippetDB.files = snippet.files != null ? snippet.files : snippetDB.files;
                snippetDB.comments = snippet.comments != null ? snippet.comments : snippetDB.comments;
                snippetDB.metas = snippet.metas != null ? snippet.metas : snippetDB.metas;
                snippetDB.lastTouchedTime = Instant.now();
            } else {
                throw new WebApplicationException(403);
            }
        }
        return Response.noContent().header("Location", UriBuilder.fromUri(codeSnippetApiURL).path("snippet").path(snippetId).build()).build();

    }

    @Transactional
    @Path("{snippetId}")
    @Produces(MediaType.TEXT_PLAIN)
    @Authenticated
    @DELETE
    public Response deleteSnippet(@PathParam("snippetId") String snippetId, @Context SecurityContext ctx) {
        Snippet res = Snippet.findById(snippetId);

        if (res == null) {
            throw new WebApplicationException(404);
        } else {
            if (res.owner.equals(ctx.getUserPrincipal().getName())) {
                res.files.stream().forEach(item -> item.delete());
                res.comments.stream().forEach(item -> item.delete());
                res.metas.stream().forEach(item -> item.delete());
                res.delete();
            } else {
                throw new WebApplicationException(403);
            }
        }
        return Response.noContent().build();
    }
}


