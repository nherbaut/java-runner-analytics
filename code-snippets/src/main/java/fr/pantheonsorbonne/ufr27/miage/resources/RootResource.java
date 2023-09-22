package fr.pantheonsorbonne.ufr27.miage.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.net.URISyntaxException;

@Path("/")
public class RootResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getRoot() throws URISyntaxException {
        return Response.seeOther(new URI("/snippet/all")).build();
    }
}
