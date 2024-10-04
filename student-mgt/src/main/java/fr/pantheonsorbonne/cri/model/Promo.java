package fr.pantheonsorbonne.cri.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Promo extends PanacheEntity {
    public String name;
    @OneToMany(mappedBy = "promo")
    public List<Course> courses = new ArrayList<>();
}
