package fr.pantheonsorbonne.ufr27.miage.resources;

import fr.pantheonsorbonne.ufr27.miage.dto.SnippetSummaryDTO;
import fr.pantheonsorbonne.ufr27.miage.dto.UserDTO;
import fr.pantheonsorbonne.ufr27.miage.model.Snippet;
import io.quarkus.resteasy.reactive.links.InjectRestLinks;
import io.quarkus.resteasy.reactive.links.RestLink;
import jakarta.ws.rs.*;

import java.util.Collection;

@Path("user")
public class UserResources {

    @Path("all")
    @GET
    public Collection<UserDTO> getAllUsers(@DefaultValue("0") @QueryParam("pageIndex") int pageIndex, @DefaultValue("25") @QueryParam("pageSize") int pageSize) {
        return Snippet.find("SELECT DISTINCT s.owner from Snippet s").project(UserDTO.class).page(pageIndex, pageSize).list();
    }


    @Path("{userId}/snippets")
    @GET
    @RestLink(rel = "list")
    @InjectRestLinks
    public Collection<SnippetSummaryDTO> getSnippetsForUser(@PathParam("userId") String userId) {


        return Snippet.find("owner = ?1", userId).project(SnippetSummaryDTO.class).list();
    }
}
