package fr.pantheonsorbonne.ufr27.miage.resources;

import fr.pantheonsorbonne.ufr27.miage.model.Meta;
import io.quarkus.panache.common.Parameters;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

import java.util.List;

@Path("meta")
public class MetaResources {

    @GET
    @Path("keys")
    @Transactional
    public List<String> getKeysSortedByOccurrences() {
        String query = "SELECT m.key FROM Meta m GROUP BY m.key ORDER BY COUNT(m.key) DESC";
        return Meta.find(query).project(String.class).list();
    }

    @GET
    @Path("values")
    @Transactional
    public List<String> getValuesForKeys(@QueryParam("key") String key) {
        String query = "SELECT m.value FROM Meta m where m.key=?1 GROUP BY m.value ORDER BY COUNT(m.value) DESC";
        return Meta.find(query,key).project(String.class).list();
    }





}
