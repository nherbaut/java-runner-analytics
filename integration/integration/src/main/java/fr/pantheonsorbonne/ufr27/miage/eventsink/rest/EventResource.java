package fr.pantheonsorbonne.ufr27.miage.eventsink.rest;

import fr.pantheonsorbonne.ufr27.miage.eventsink.model.Event;
import fr.pantheonsorbonne.ufr27.miage.eventsink.service.EventGateway;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;


@Path("/rest/event")
public class EventResource {

    @Inject
    EventGateway gateway;

    @Produces(MediaType.TEXT_PLAIN)
    @Path("/")
    @POST
    @RolesAllowed("recaptcha-cleared")
    @Consumes(MediaType.APPLICATION_JSON)
    public void postEvent(Event event) {
        gateway.publishEvent(event);

    }
}
