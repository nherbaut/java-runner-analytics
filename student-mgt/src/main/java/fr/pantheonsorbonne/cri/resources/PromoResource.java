package fr.pantheonsorbonne.cri.resources;

import fr.pantheonsorbonne.cri.model.Promo;
import fr.pantheonsorbonne.cri.model.Student;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;
import io.smallrye.common.annotation.Blocking;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;

@Path("promo")
@Authenticated
public class PromoResource {

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance promo(List<Promo> promos);
    }


    @GET
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    @Blocking
    @RolesAllowed("admin-java")
    public TemplateInstance getPromos() {
        List<Promo> promos = Promo.findAll().list();
        return Templates.promo(promos);

    }
}
