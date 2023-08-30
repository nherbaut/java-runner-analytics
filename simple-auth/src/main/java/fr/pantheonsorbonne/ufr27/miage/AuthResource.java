package fr.pantheonsorbonne.ufr27.miage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.pantheonsorbonne.ufr27.miage.auth.RecaptchaEnterpriseAssessment;
import fr.pantheonsorbonne.ufr27.miage.auth.RecaptchaEnterpriseEvent;
import fr.pantheonsorbonne.ufr27.miage.auth.RecaptchaResponse;
import fr.pantheonsorbonne.ufr27.miage.auth.RecaptchaVerifyService;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Path("auth")
public class AuthResource {


    @RestClient
    RecaptchaVerifyService recaptchaVerifyService;


    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance auth(String reCAPTCHA_site_key);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get() {
        String siteKey = System.getenv("RECAPTCHA_SITE_KEY");
        return Templates.auth(siteKey);
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String post(@HeaderParam("RECAPTCH_TOKEN") String token, @HeaderParam("UUID") String uuid, @HeaderParam("UUID") String action) throws JsonProcessingException {
        String googleApiKey = System.getenv("GOOGLE_API_KEY");
        String siteKey = System.getenv("RECAPTCHA_SITE_KEY");
        RecaptchaEnterpriseEvent event = new RecaptchaEnterpriseEvent(token, siteKey, action);
        RecaptchaEnterpriseAssessment assessment = new RecaptchaEnterpriseAssessment(event);
        RecaptchaResponse resp = recaptchaVerifyService.verify(googleApiKey, "java-runner", assessment);
        if (Double.parseDouble(resp.riskAnalysis().score()) >= 0.7) {
            JwtClaimsBuilder builder1 = Jwt.claims();
            builder1.groups("recaptcha-cleared").preferredUserName("nicolas").expiresAt(Instant.now().plus(1L, TimeUnit.DAYS.toChronoUnit())).issuer("https://java.miage.dev");


            return builder1.sign();
        } else {
            throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity("recaptcha score prevented issuing JWT Token: score is " + resp.riskAnalysis().score() + " reasons are " + resp.riskAnalysis().reasons().stream().collect(Collectors.joining(";"))).build());
        }

    }
}
