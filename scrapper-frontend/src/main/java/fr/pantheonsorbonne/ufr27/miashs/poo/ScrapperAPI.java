package fr.pantheonsorbonne.ufr27.miashs.poo;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("scrapper")
@RegisterRestClient(configKey = "scrapping-function")
public interface ScrapperAPI {
    @GET
    @Produces(MediaType.TEXT_HTML)
    String getURL(String url);
}
