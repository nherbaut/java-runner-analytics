package fr.pantheonsorbonne.ufr27.miage.eventsink.model;

import com.fasterxml.jackson.databind.JsonNode;

public record Event(String type, JsonNode payload) {
}
