package fr.pantheonsorbonne.ufr27.miage.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Comment extends PanacheEntity {

    public String content;
}
