package fr.pantheonsorbonne.ufr27.miage;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HEAD;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.stream.Collectors;

@Path("claims-check")
public class CheckClaimResource {
    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance root();
    }

    @Inject
    protected JsonWebToken jwt;

    @PermitAll
    @GET
    public Response getUserInfo(@Context SecurityContext context) {
        Response.ResponseBuilder builder = Response.ok();
        builder = builder.header("preferred_name", context.getUserPrincipal().getName());
        builder = builder.header("groups", jwt.getGroups().stream().collect(Collectors.joining(",")));
        return builder.build();
    }


}
