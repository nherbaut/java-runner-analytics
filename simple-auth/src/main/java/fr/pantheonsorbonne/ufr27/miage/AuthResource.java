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
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.ClientWebApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Path("/")
public class AuthResource {

    @ConfigProperty(name="mp.jwt.verify.issuer")
    String issuer;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthResource.class);

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
    public String post(@Nonnull @HeaderParam("RECAPTCH-TOKEN") String token, @Nonnull @HeaderParam("UUID") String uuid, @Nonnull @HeaderParam("action") String action,@Context HttpHeaders headers) throws JsonProcessingException {

        headers.getRequestHeaders().entrySet().stream().forEach( e -> {
            LOGGER.debug("Headers for Query = {}:{}",e.getKey(),e.getValue().stream().collect(Collectors.joining(";")));
        });


        LOGGER.info("received token {} for validation, {} for uuid and {} for action", token, uuid, action);
        String googleApiKey = System.getenv("GOOGLE_API_KEY");
        String siteKey = System.getenv("RECAPTCHA_SITE_KEY");
        LOGGER.debug("Google credentials are {} {}", googleApiKey, siteKey);
        RecaptchaEnterpriseEvent event = new RecaptchaEnterpriseEvent(token, siteKey, action);
        RecaptchaEnterpriseAssessment assessment = new RecaptchaEnterpriseAssessment(event);
        try {
            LOGGER.debug("Querying assessment: {}", assessment);
            RecaptchaResponse resp = recaptchaVerifyService.verify(googleApiKey, "java-runner", assessment);

            if (Double.parseDouble(resp.riskAnalysis().score()) >= 0.7) {
                JwtClaimsBuilder builder1 = Jwt.claims();
                builder1.groups("recaptcha-cleared").preferredUserName(uuid).expiresAt(Instant.now().plus(1L, TimeUnit.DAYS.toChronoUnit())).issuer(issuer);


                return builder1.sign();
            } else {
                throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity("recaptcha score prevented issuing JWT Token: score is " + resp.riskAnalysis().score() + " reasons are " + resp.riskAnalysis().reasons().stream().collect(Collectors.joining(";"))).build());
            }
        } catch (Throwable eps) {
            LOGGER.error("failed to verify assessment " + assessment.toString() + " {}", Arrays.stream(eps.getStackTrace()).map(e -> e.toString()).collect(Collectors.joining("\n")), eps);
            throw eps;
        }
    }
}
