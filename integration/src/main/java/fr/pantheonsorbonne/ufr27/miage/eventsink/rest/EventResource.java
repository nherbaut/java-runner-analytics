package fr.pantheonsorbonne.ufr27.miage.eventsink.rest;

import fr.pantheonsorbonne.ufr27.miage.eventsink.model.Event;
import fr.pantheonsorbonne.ufr27.miage.eventsink.model.EventDTO;
import fr.pantheonsorbonne.ufr27.miage.eventsink.service.EventGateway;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestResponse;


@Path("/rest/event")
public class EventResource {

    @Inject
    EventGateway gateway;

    @Produces(MediaType.TEXT_PLAIN)
    @Path("/")
    @POST
    @RolesAllowed({"student-events-opt-in"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<RestResponse<Event>> postEvent(EventDTO event) {
        return gateway.publishEvent(event).replaceWith(RestResponse.status(RestResponse.Status.CREATED));

    }
}
