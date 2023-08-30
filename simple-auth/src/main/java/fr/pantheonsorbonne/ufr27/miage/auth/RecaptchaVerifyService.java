package fr.pantheonsorbonne.ufr27.miage.auth;

import jakarta.annotation.Nullable;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("v1/projects")
@RegisterRestClient
public interface RecaptchaVerifyService {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{projectId}/assessments")
    RecaptchaResponse verify(@QueryParam("key") String apiKey, @PathParam("projectId") String projectId,RecaptchaEnterpriseAssessment assessment);


}
