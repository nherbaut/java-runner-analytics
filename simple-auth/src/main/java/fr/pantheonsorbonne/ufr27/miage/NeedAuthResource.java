package fr.pantheonsorbonne.ufr27.miage;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("private")
public class NeedAuthResource {

    @GET
    @RolesAllowed("recaptcha-cleared")
    public String get(SecurityContext ctx) {

        return "ok";
    }
}
