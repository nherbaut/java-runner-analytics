package fr.pantheonsorbonne.ufr27.miage.resources;

import fr.pantheonsorbonne.ufr27.miage.model.Snippet;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.net.URI;
import java.util.Collection;
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


    @Path("{snippetId}")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @PermitAll
    public Snippet getSnippet(@PathParam("snippetId") Integer snippetId) {
        Snippet res = Snippet.findById(snippetId);
        if (res == null) {
            throw new WebApplicationException(404);
        } else {
            return res;
        }
    }

    @Transactional
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response postSnippet(Snippet snippet, @Context SecurityContext ctx) {
        snippet.owner = ctx.getUserPrincipal().getName();
        Snippet.persist(snippet);
        return Response.created(URI.create("/snippet/" + snippet.id)).build();
    }

    @Transactional
    @PUT
    @Authenticated
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateSnippet(Snippet snippet, @Context SecurityContext ctx) {
        Snippet snippetDB = Snippet.findById(snippet.id);
        if (snippetDB == null) {
            throw new WebApplicationException(404);
        } else {
            if (Objects.equals(snippetDB.owner, ctx.getUserPrincipal().getName())) {
                snippet.persistAndFlush();
            } else {
                throw new WebApplicationException(403);
            }
        }
        return Response.ok().build();

    }

    @Transactional
    @Path("{snippetId}")
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


