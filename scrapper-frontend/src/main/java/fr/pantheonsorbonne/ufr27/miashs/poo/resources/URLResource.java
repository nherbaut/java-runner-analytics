package fr.pantheonsorbonne.ufr27.miashs.poo.resources;

import fr.pantheonsorbonne.ufr27.miashs.poo.business.WebPageSourceCodeService;
import io.quarkus.cache.CacheResult;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;

@Path("/url")
public class URLResource {

    @Inject
    WebPageSourceCodeService webPageSourceCodeService;

    @Path("{base64URL}")
    @GET
    @CacheResult(cacheName = "url-cache")
    @Produces(MediaType.TEXT_HTML)
    public String getScrappedContent(@PathParam("base64URL") String base64) {
        return webPageSourceCodeService.getURLContent(new String(Base64.getUrlDecoder().decode(base64)));
    }



    @ConfigProperty(name="server-uri",defaultValue = "/")
    String serverURI;
    @CacheResult(cacheName = "url-cache")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.TEXT_PLAIN})
    @POST
    public Response postScrappedContent(String url, @Context UriInfo uriInfo) {
        try {


            return Response.seeOther(new URI(serverURI+"url/" + Base64.getUrlEncoder().encodeToString(url.getBytes()))).build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

}
