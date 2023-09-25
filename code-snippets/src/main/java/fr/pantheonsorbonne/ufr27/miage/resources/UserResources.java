package fr.pantheonsorbonne.ufr27.miage.resources;

import fr.pantheonsorbonne.ufr27.miage.dto.SnippetSummaryDTO;
import fr.pantheonsorbonne.ufr27.miage.dto.UserDTO;
import fr.pantheonsorbonne.ufr27.miage.model.Snippet;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;
import io.smallrye.jwt.auth.principal.DefaultJWTParser;
import io.smallrye.jwt.auth.principal.JWTAuthContextInfo;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import io.smallrye.jwt.config.JWTAuthContextInfoProvider;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import org.apache.commons.codec.binary.Base64;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jose4j.keys.resolvers.VerificationKeyResolver;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Collection;
import java.util.List;

@Path("user")
public class UserResources {

    @Path("all")
    @GET
    public Collection<UserDTO> getAllUsers(@DefaultValue("0") @QueryParam("pageIndex") int pageIndex, @DefaultValue("25") @QueryParam("pageSize") int pageSize) {
        return Snippet.find("SELECT DISTINCT s.owner from Snippet s").project(UserDTO.class).page(pageIndex, pageSize).list();
    }


    @Path("{userId}/snippets")
    @GET
    public Collection<SnippetSummaryDTO> getSnippetsForUser(@PathParam("userId") String userId) {


        return Snippet.find("owner = ?1", userId).project(SnippetSummaryDTO.class).list();
    }


    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance index(List<Snippet> snippets, String codeSnippetApiURL);
    }


    @ConfigProperty(name = "fr.pantheonsorbonne.miage.codeSnippetAPIURL")
    String codeSnippetApiURL;

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Authenticated
    @Path("me")
    public TemplateInstance getHome(@Context SecurityContext context) throws Exception {


        List<Snippet> snippets = Snippet.find("SELECT s from Snippet s where s.owner=?1", context.getUserPrincipal().getName()).list();
        return Templates.index(snippets, codeSnippetApiURL);
    }

}
