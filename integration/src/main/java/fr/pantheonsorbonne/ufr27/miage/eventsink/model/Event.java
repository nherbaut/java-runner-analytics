package fr.pantheonsorbonne.ufr27.miage.eventsink.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public record Event(@JsonProperty(defaultValue = "") String source, @JsonProperty(defaultValue = "") String application,
                    @JsonProperty(defaultValue = "") String userID, String type, JsonNode payload) {
}
