package fr.pantheonsorbonne.ufr27.miashs.poo;

import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheResult;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


@Path("/url")
public class URLResource {

    @RestClient
    ScrapperAPI api;

    @Inject
    PrettyPrintService prettyPrintService;

    @CacheResult(cacheName = "url-cache")
    @POST
    @Produces(MediaType.TEXT_HTML)
    public String getScrappedContent(String url) {
        return prettyPrintService.prettyHTML(api.getURL(url));

    }

    @CacheInvalidate(cacheName = "url-cache")
    @Path("invalidate")
    @POST
    public Response invalidateCacheForURL(String url) {

        return Response.accepted().build();
    }
}
