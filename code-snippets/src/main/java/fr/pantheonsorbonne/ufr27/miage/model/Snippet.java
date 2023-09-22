package fr.pantheonsorbonne.ufr27.miage.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.util.Collection;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Snippet extends PanacheEntity {

    public String title;
    public String owner;

    @OneToMany(cascade = CascadeType.ALL)
    public Collection<File> files;
    @OneToMany(cascade = CascadeType.ALL)
    public Collection<Comment> comments;
}
