package fr.pantheonsorbonne.ufr27.miage.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Collection;
import java.util.List;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Snippet extends PanacheEntityBase {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    public String id;
    public String title;
    public String owner;

    @OneToMany(cascade = CascadeType.ALL)
    public List<File> files;
    @OneToMany(cascade = CascadeType.ALL)
    public List<Comment> comments;
}
