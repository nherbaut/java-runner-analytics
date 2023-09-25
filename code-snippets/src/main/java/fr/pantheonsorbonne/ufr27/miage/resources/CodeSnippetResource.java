package fr.pantheonsorbonne.ufr27.miage.resources;

import fr.pantheonsorbonne.ufr27.miage.model.Snippet;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.apache.http.client.utils.URIBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Path("snippet")
public class CodeSnippetResource {


    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @PermitAll
    public Collection<Snippet> getAllSnippetsPaged(@DefaultValue("0") @QueryParam("pageIndex") int pageIndex, @DefaultValue("25") @QueryParam("pageSize") int pageSize) {
        return Snippet.findAll().page(pageIndex, pageSize).list();

    }

    @Path("mine")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Authenticated
    public Collection<Snippet> getMySnippets(@Context SecurityContext context, @DefaultValue("0") @QueryParam("pageIndex") int pageIndex, @DefaultValue("25") @QueryParam("pageSize") int pageSize) {

        return Snippet.find("SELECT s from Snippet s WHERE s.owner=?1", context.getUserPrincipal().getName()).page(pageIndex, pageSize).list();

    }

    @ConfigProperty(name = "fr.pantheonsorbonne.miage.codeSnippetAPIURL")
    String codeSnippetApiURL;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance showSnippet(Snippet snippet, String codeSnippetApiURL, String userId);
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

    @Path("{snippetId}")
    @Produces(MediaType.TEXT_HTML)
    @GET
    @PermitAll
    public TemplateInstance getSnippetHTML(@PathParam("snippetId") String snippetId, @Context SecurityContext securityContext) {
        String userId = null;
        if (securityContext.getUserPrincipal() != null) {
            userId = securityContext.getUserPrincipal().getName();
        }
        Snippet snippet = this.getSnippet(snippetId);
        return Templates.showSnippet(snippet, codeSnippetApiURL, userId);
    }


    @Transactional
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Authenticated
    public Response postSnippet(Snippet snippet, @Context SecurityContext ctx) {
        snippet.owner = ctx.getUserPrincipal().getName();
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
            if (Objects.equals(snippetDB.owner, ctx.getUserPrincipal().getName())) {
                snippetDB.title = snippet.title;
                snippetDB.files = snippet.files;
                snippetDB.comments = snippet.comments;
            } else {
                throw new WebApplicationException(403);
            }
        }
        return Response.noContent().build();

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
                res.delete();
            } else {
                throw new WebApplicationException(403);
            }
        }
        return Response.noContent().build();
    }
}


