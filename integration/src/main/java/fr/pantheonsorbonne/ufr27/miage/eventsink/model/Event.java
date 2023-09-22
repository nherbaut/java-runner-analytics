package fr.pantheonsorbonne.ufr27.miage.eventsink.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;

@Entity
public class Event extends PanacheEntityBase {
    public String source;
    public String application;
    public String userId;
    public String type;
    @Column(length = 16_777_216)
    public String payload;

    public Instant instant;
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    private String id;


    @Override
    public String toString() {
        return "Event{" +
                "source='" + source + '\'' +
                ", application='" + application + '\'' +
                ", userId='" + userId + '\'' +
                ", type='" + type + '\'' +
                ", payload='" + payload + '\'' +
                ", instant=" + instant +
                ", id='" + id + '\'' +
                '}';
    }
}
