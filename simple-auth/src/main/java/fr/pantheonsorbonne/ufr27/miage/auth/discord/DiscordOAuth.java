package fr.pantheonsorbonne.ufr27.miage.auth.discord;

import io.quarkus.oidc.client.Tokens;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Path("discord")
public class DiscordOAuth {

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    @RestClient
    DiscordAPI discordAPI;

    volatile Tokens currentTokens;

    @ConfigProperty(name = "quarkus.oidc-client.client-id")
    String clientId;

    @ConfigProperty(name = "quarkus.oidc-client.credentials.secret")
    String clientSecret;

    @ConfigProperty(name = "quarkus.oidc-client.scopes")
    String scopes;

    @ConfigProperty(name = "fr.pantheonsorbonne.ufr27.miage.discord-redirect-uri")
    String redirectURI;

    @ConfigProperty(name = "fr.pantheonsorbonne.ufr27.miage.discord-authorization-uri")
    String discordAuthURI;
    @Inject
    StateManager stateManager;

    @ConfigProperty(name = "fr.pantheonsorbonne.ufr27.miage.admin-emails")
    List<String> adminEmails;


    @GET
    public Response getAuth(@QueryParam("callback") String callback) {

        String state = Base64.getEncoder().encodeToString((UUID.randomUUID().toString() + "," + callback).getBytes(StandardCharsets.UTF_8));

        stateManager.pushState(state);

        URI redirectDiscordURI = UriBuilder.fromUri(discordAuthURI).queryParam("state", state).build();

        return Response.seeOther(redirectDiscordURI).build();
    }

    @GET
    @Path("callback")
    @Produces(MediaType.TEXT_PLAIN)
    public Response handleCode(@QueryParam("code") String code, @QueryParam("state") String state) throws IOException, ExecutionException, InterruptedException, TimeoutException {

        if (Objects.nonNull(state) && stateManager.popState(state)) {

            DiscordAccessTokenResponse resp = discordAPI.getDiscordResource(clientId, clientSecret, code, "authorization_code", redirectURI);

            DiscordUserResponse discordUser = discordAPI.getDiscordUser("Bearer " + resp.accessToken());

            JwtClaimsBuilder builder1 = Jwt.claims();
            JwtClaimsBuilder claimsBuilder = builder1
                    .claim("discord-token", resp.accessToken())
                    .claim("discord-refresh-token", resp.refreshToken())
                    .claim("discord-expires-in", resp.expiresIn())
                    .preferredUserName(discordUser.global_name())
                    .claim(Claims.email, discordUser.email())
                    .expiresAt(Instant.now().plus(1L, TimeUnit.DAYS.toChronoUnit())).issuer(issuer);

            if (adminEmails.contains(discordUser.email())) {
                claimsBuilder.groups(Set.of("discord-auth", "helpers"));
            }
            else{
                claimsBuilder.groups(Set.of("discord-auth"));
            }
            String token = claimsBuilder.sign();
            String referer = new String(Base64.getDecoder().decode(state.getBytes())).split(",")[1];
            URI redirectURI = UriBuilder.fromUri(referer).queryParam("token", token).build();
            return Response.seeOther(redirectURI).build();
        } else {
            return Response.status(400, "Failed to validate state").build();
        }


    }


}
