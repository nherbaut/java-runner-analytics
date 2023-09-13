package fr.pantheonsorbonne.ufr27.miage;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

import java.io.ByteArrayInputStream;

@Priority(Priorities.USER)
public class RestClientExceptionMapper
        implements ResponseExceptionMapper<WebApplicationException> {

    @Override
    public WebApplicationException toThrowable(Response response) {
        try {
            response.bufferEntity();
        } catch (Exception ignored) {
        }
        String msg = getBody(response);
        return new WebApplicationException(msg, response);
    }

    @Override
    public boolean handles(int status, MultivaluedMap<String, Object> headers) {
        return status >= 400;
    }

    private String getBody(Response response) {
        return response.readEntity(String.class);
    }
}