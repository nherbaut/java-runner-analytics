package fr.pantheonsorbonne.ufr27.miage.auth.discord;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;

public record DiscordAccessTokenExchangePayload(@JsonProperty("client_id") String clientId,
                                                @JsonProperty("client_secret") String clienSecret,
                                                String code,
                                                @JsonProperty("grant_type") String grantType,

                                                @JsonProperty("redirect_uri") String redirectUri) {

    public String toJson() throws IOException {

        try (StringWriter stringWriter = new StringWriter()) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(stringWriter, this);
            return stringWriter.toString();
        }

    }
}
