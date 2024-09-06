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

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getHome(@QueryParam("callback") URI
                                        callback) {
        return Response.seeOther(UriBuilder.fromPath("/oidc").queryParam("callback", callback).build()).build();

    }


}
