package fr.pantheonsorbonne.ufr27.miashs.poo.business;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import org.apache.http.impl.client.HttpClients;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.plugins.interceptors.AcceptEncodingGZIPFilter;
import org.jboss.resteasy.plugins.interceptors.GZIPDecodingInterceptor;
import org.jboss.resteasy.plugins.interceptors.GZIPEncodingInterceptor;

import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class WebPageSourceCodeService {

    @Inject
    PrettyPrintService prettyPrintService;

    public String getURLContent(String url ) {
        return getURLContent(url,new HashMap<>());
    }
    public String getURLContent(String url, Map<String, NewCookie> cookies) {
        System.out.println(url);
        cookies.entrySet().forEach( e -> {
            System.out.print(e.getKey());
            System.out.print(": ");
            System.out.println(e.getValue().getValue());
        });

        Client client = ((ResteasyClientBuilder) ClientBuilder.newBuilder())
                .enableCookieManagement()

                .register(AcceptEncodingGZIPFilter.class)
                .register(GZIPDecodingInterceptor.class)
                .register(GZIPEncodingInterceptor.class)
                .build();


        WebTarget target = client.target(url);


        Invocation.Builder builder = target.request(MediaType.TEXT_HTML).header("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:109.0) Gecko/20100101 Firefox/119.0");
        for(Map.Entry<String,NewCookie> entry : cookies.entrySet()){
            builder.cookie(entry.getValue());
        }
        Response response = builder.get();

        if(response.getStatus()>=200 && response.getStatus()<300) {
            return prettyPrintService.prettyHTML(response.readEntity(String.class));
        }
        else if(response.getStatus()>=300 && response.getStatus()<400){
            var redirectCookies = response.getCookies();
            String newUrl=response.getHeaders().entrySet().stream().filter(e->e.getKey().toLowerCase().equals("location")).findAny().orElseThrow().getValue().stream().findAny().orElseThrow().toString();
            return getURLContent(newUrl,redirectCookies);

        }
        else {
            System.out.println(response.readEntity(String.class));
            throw new WebApplicationException("Impossible de scrapper, la page retourne " + response.getStatus(), 404);
        }

    }
}
