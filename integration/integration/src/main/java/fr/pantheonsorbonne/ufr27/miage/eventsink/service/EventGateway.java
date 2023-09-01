package fr.pantheonsorbonne.ufr27.miage.eventsink.service;

import fr.pantheonsorbonne.ufr27.miage.eventsink.model.Event;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class EventGateway {

    private final static Logger LOGGER = LoggerFactory.getLogger(EventGateway.class);

    @Inject
    @Channel("integration")
    Emitter<Event> emitter;

    public void publishEvent(Event event) {
        LOGGER.info("event published {}", event.toString());
        emitter.send(event);
    }
}


