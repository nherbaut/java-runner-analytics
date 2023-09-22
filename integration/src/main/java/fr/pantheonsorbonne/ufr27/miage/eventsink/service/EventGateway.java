package fr.pantheonsorbonne.ufr27.miage.eventsink.service;

import fr.pantheonsorbonne.ufr27.miage.eventsink.model.Event;
import fr.pantheonsorbonne.ufr27.miage.eventsink.model.EventDTO;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

@ApplicationScoped
public class EventGateway {

    private final static Logger LOGGER = LoggerFactory.getLogger(EventGateway.class);


    @WithSession
    @WithTransaction
    public Uni<Void> publishEvent(EventDTO eventDTO) {
        Event event = new Event();
        event.payload = eventDTO.payload().toPrettyString();
        event.source = eventDTO.source();
        event.type = eventDTO.type();
        event.userId = eventDTO.userID();
        event.application = eventDTO.application();
        event.instant= Instant.now();


        return Event.persist(event);
    }
}


