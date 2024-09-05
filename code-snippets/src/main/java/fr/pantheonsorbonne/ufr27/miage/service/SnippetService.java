package fr.pantheonsorbonne.ufr27.miage.service;

import fr.pantheonsorbonne.ufr27.miage.model.Snippet;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@ApplicationScoped
public class SnippetService {

    public List<Snippet> searchSnippets(Map<String, String> queryParams) {
        if (queryParams.isEmpty()) {
            return List.of(); // return an empty list if no query parameters are provided
        }

        StringJoiner queryBuilder = new StringJoiner(" and ");
        Parameters parameters = new Parameters();

        int paramIndex = 0;
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            queryBuilder.add("metas.key = :key" + paramIndex + " and metas.value = :value" + paramIndex);
            parameters.and("key" + paramIndex, key).and("value" + paramIndex, value);
            paramIndex++;
        }

        String query = queryBuilder.toString();
        return Snippet.find("select distinct s from Snippet s join s.metas metas where " + query, parameters).list();
    }
}
