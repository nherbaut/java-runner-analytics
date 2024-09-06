package fr.pantheonsorbonne.ufr27.miage;

import io.quarkus.oidc.UserInfo;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;
import io.smallrye.jwt.auth.principal.DefaultJWTCallerPrincipal;
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





    @Authenticated
    @GET
    public Response getUserInfo(@Context SecurityContext context) {
        Response.ResponseBuilder builder = Response.ok();
        DefaultJWTCallerPrincipal principal = (DefaultJWTCallerPrincipal)context.getUserPrincipal();
        builder = builder.header("preferred_name", principal.getName());
        builder = builder.header("groups", String.join(",", principal.getGroups()));
        return builder.build();
    }


}
