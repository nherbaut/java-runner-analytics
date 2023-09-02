package fr.pantheonsorbonne.ufr27.miage.eventsink.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import fr.pantheonsorbonne.ufr27.miage.eventsink.model.Event;
import fr.pantheonsorbonne.ufr27.miage.eventsink.service.EventGateway;
import fr.pantheonsorbonne.ufr27.miage.eventsink.ws.auth.WebSocketSecurityConfigurator;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.IdentityProvider;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.runtime.SecurityIdentityAssociation;
import io.smallrye.jwt.auth.cdi.PrincipalProducer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@ServerEndpoint(value = "/ws/event", configurator = WebSocketSecurityConfigurator.class)
@ApplicationScoped
public class EventSocket {

    private final static Logger LOGGER = LoggerFactory.getLogger(EventSocket.class);
    private final static ObjectReader jsonWriter = new ObjectMapper().reader();
    @Inject
    EventGateway gateway;


    @Authenticated
    @OnOpen
    public void onOpen(Session session) {
        LOGGER.error("connection opened");
    }


    @OnClose
    public void onClose(Session session) {
        LOGGER.info("onClose");
    }

    @OnError
    public void onError(Session session, Throwable t) {
        LOGGER.info("onError", t);
    }

    @Inject
    SecurityIdentityAssociation identity;

    @OnMessage
    public void onMessage(final Session session, String message) throws IOException {

        if ("keepalive".equals(message)) {
            return;
        }


        Event eventToPublish = createEventFromMessage(message);
        gateway.publishEvent(eventToPublish);


    }

    private Event createEventFromMessage(String message) throws IOException {
        Event eventFromApplication = jsonWriter.readValue(message, Event.class);
        Event eventToPublish = new Event(EventSocket.class.getName(), eventFromApplication.application(), identity.getIdentity().getPrincipal().getName(), eventFromApplication.type(), eventFromApplication.payload());
        return eventToPublish;
    }


}

