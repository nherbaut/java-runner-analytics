package fr.pantheonsorbonne.ufr27.miashs.poo.resources;

import fr.pantheonsorbonne.ufr27.miashs.poo.business.ProjectService;
import fr.pantheonsorbonne.ufr27.miashs.poo.business.WebPageSourceCodeService;
import fr.pantheonsorbonne.ufr27.miashs.poo.dao.ProjetDAO;
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

    @Inject
    ProjectService projectService;

    @Path("{base64URL}")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getScrappedContent(@PathParam("base64URL") String base64) {
        String decodedURL = new String(Base64.getUrlDecoder().decode(base64.replace("%2f","/")));
        String ownerInfo = null;
        try {
            ownerInfo = projectService.getProjectOwnerInfo(decodedURL);
            Response.ResponseBuilder builder = Response.ok(webPageSourceCodeService.getURLContent(decodedURL));
            if (ownerInfo != null) {
                builder.header("X-OWNER-INFO", ownerInfo);
            }
            return builder.build();
        } catch (URISyntaxException e) {
            throw new WebApplicationException("invalid uri  " + decodedURL, 400);
        }

    }

    @Inject
    ProjetDAO projetDAO;

    @ConfigProperty(name = "server-uri", defaultValue = "/")
    String serverURI;

    @CacheResult(cacheName = "url-cache")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.TEXT_PLAIN})
    @POST
    public Response postScrappedContent(String url) {
        try {


            var responseBuilder = Response.seeOther(new URI(serverURI + "url/" + Base64.getUrlEncoder().encodeToString(url.getBytes()).replace("/","%2f")));

            return responseBuilder.build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

}
