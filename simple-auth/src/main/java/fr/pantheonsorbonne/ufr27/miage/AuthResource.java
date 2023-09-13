package fr.pantheonsorbonne.ufr27.miage;

import fr.pantheonsorbonne.ufr27.miage.auth.discord.StateManager;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.config.inject.ConfigProperty;


@Path("/")
public class AuthResource {

    @ConfigProperty(name = "fr.pantheonsorbonne.ufr27.miage.discord-authorization-uri")
    String redirectURI;

    @Inject
    StateManager stateManager;


    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance index(String callback);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getHome(@QueryParam("callback") String callback) {
        return Templates.index(callback);
    }


}
