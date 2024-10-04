package fr.pantheonsorbonne.cri.resources;

import io.quarkus.security.Authenticated;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.net.URI;

@Path("/")
@Authenticated
public class RouteResource {

    @GET
    public Response getRoot(@Context SecurityContext securityContext) {
        if (securityContext.isUserInRole("admin-java")) {
            return Response.seeOther(URI.create("/promo")).build();
        } else {
            return Response.seeOther(URI.create("/students/me")).build();
        }
    }
}
