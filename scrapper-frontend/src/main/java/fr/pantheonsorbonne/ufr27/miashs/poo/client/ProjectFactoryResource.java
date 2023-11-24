package fr.pantheonsorbonne.ufr27.miashs.poo.client;

import fr.pantheonsorbonne.ufr27.miashs.poo.model.InjectedFile;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.io.IOException;
import java.util.List;

@Path("/")
@RegisterRestClient(configKey = "maven-project-factory")
public interface ProjectFactoryResource {
    @Path("{project-template}")
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Response post(@PathParam("project-template") String template, List<InjectedFile> assets);


}
