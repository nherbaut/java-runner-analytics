package fr.pantheonsorbonne.ufr27.miage.auth.discord;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DiscordAccessTokenResponse(@JsonProperty("access_token") String accessToken,
                                         @JsonProperty("token_type") String tokenType,
                                         @JsonProperty("expires_in") Integer expiresIn,
                                         @JsonProperty("refresh_token") String refreshToken,
                                         @JsonProperty("scope") String scope) {
}
