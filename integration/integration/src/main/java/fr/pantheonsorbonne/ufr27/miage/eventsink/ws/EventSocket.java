package fr.pantheonsorbonne.ufr27.miage.eventsink.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.pantheonsorbonne.ufr27.miage.eventsink.model.Event;
import fr.pantheonsorbonne.ufr27.miage.eventsink.service.EventGateway;
import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ServerEndpoint(value = "/ws/event", configurator = WebSocketSecurityConfigurator.class)
@ApplicationScoped
public class EventSocket {

    private final static Logger LOGGER = LoggerFactory.getLogger(EventSocket.class);

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


    @OnMessage
    public void onMessage(String message) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        Event event = mapper.readValue(message, Event.class);
        gateway.publishEvent(event);


    }


}

