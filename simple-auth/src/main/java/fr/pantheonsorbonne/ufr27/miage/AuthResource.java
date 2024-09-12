package fr.pantheonsorbonne.ufr27.miage;

import fr.pantheonsorbonne.ufr27.miage.auth.discord.StateManager;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.net.URI;


@Path("/")
public class AuthResource {


    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance index(String callback);
    }

    @ConfigProperty(name="fr.pantheonsorbonne.ufr27.miage.authuri")
    URI authuri;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getHome(@QueryParam("callback") URI
                                        callback,@Context UriInfo uriInfo) {

        return Response.seeOther(UriBuilder.fromUri(authuri).path("oidc").queryParam("callback", callback).build()).build();

    }


}
