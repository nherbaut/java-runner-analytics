package fr.pantheonsorbonne.ufr27.miage;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HEAD;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("claims-check")
public class CheckClaimResource {
    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance root();
    }

    @RolesAllowed({"discord-auth", "recaptcha-cleared"})
    @HEAD
    public Response getRoles(@Context SecurityContext context) {
        return Response.status(Response.Status.OK).build();
    }

}
