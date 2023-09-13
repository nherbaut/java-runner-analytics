package fr.pantheonsorbonne.ufr27.miage.auth.discord;

import fr.pantheonsorbonne.ufr27.miage.RestClientExceptionMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;


@RegisterRestClient
@RegisterProvider(value = RestClientExceptionMapper.class)
public interface DiscordAPI {
    @Path("/oauth2/token")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)

    public DiscordAccessTokenResponse getDiscordResource(@FormParam("client_id") String clientId,
                                                         @FormParam("client_secret") String clientSecret,
                                                         @FormParam("code") String code,
                                                         @FormParam("grant_type") String grantType,
                                                         @FormParam("redirect_uri") String redirectUri
    );

    @Path("/users/@me")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public DiscordUserResponse getDiscordUser(@HeaderParam("Authorization") String authorization);
}
