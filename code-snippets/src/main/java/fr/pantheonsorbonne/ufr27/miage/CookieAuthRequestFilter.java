package fr.pantheonsorbonne.ufr27.miage;


import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Priority(Priorities.AUTHENTICATION+1)
@Provider
@PreMatching

public class CookieAuthRequestFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (requestContext.getCookies().containsKey("auth-token") && !requestContext.getHeaders().containsKey("Authorization")) {
            requestContext.getHeaders().add("Authorization", "Bearer " + requestContext.getCookies().get("auth-token").getValue());

        }

    }
}
