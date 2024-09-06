package fr.pantheonsorbonne.ufr27.miage.eventsink.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import fr.pantheonsorbonne.ufr27.miage.eventsink.model.EventDTO;
import fr.pantheonsorbonne.ufr27.miage.eventsink.service.EventGateway;
import fr.pantheonsorbonne.ufr27.miage.eventsink.ws.auth.WebSocketSecurityConfigurator;
import io.quarkus.security.Authenticated;
import io.quarkus.security.runtime.SecurityIdentityAssociation;
import io.quarkus.vertx.core.runtime.context.VertxContextSafetyToggle;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.common.vertx.VertxContext;

import io.smallrye.mutiny.Uni;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.function.Supplier;

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
        LOGGER.info("connection opened");
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

    @Inject
    Vertx vertx;

    @OnMessage
    @RolesAllowed({"student-events-opt-in"})
    public void onMessage(final Session session, String message) throws IOException {


        if ("keepalive".equals(message)) {
            return;
        }

        EventDTO eventToPublish = createEventFromMessage(message);
        Context context = VertxContext.getOrCreateDuplicatedContext(vertx);
        VertxContextSafetyToggle.setContextSafe(context, Boolean.TRUE);
        context.runOnContext(x -> {
            gateway.publishEvent(eventToPublish).subscribe().with(panacheEntityBase -> LOGGER.trace("Persisted " + panacheEntityBase));
        });


    }

    private EventDTO createEventFromMessage(String message) throws IOException {
        EventDTO eventFromApplication = jsonWriter.readValue(message, EventDTO.class);
        EventDTO eventToPublish = new EventDTO(EventSocket.class.getName(), eventFromApplication.application(), identity.getIdentity().getPrincipal().getName(), eventFromApplication.type(), eventFromApplication.payload());
        return eventToPublish;
    }


}

