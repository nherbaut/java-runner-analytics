package fr.pantheonsorbonne.ufr27.miage;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("auth/claims-check/")
public class CheckClaimResource {
    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance root();
    }

    @Path("recaptcha-cleared")
    @RolesAllowed("recaptcha-cleared")
    @POST
    public Response checkRecaptcha(@Context SecurityContext context) {
        return Response.status(Response.Status.OK).build();
    }

}
