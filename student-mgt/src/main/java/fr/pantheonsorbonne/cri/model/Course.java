package fr.pantheonsorbonne.cri.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Course extends PanacheEntity {
    public String name;
    public String description;
    @OneToMany(mappedBy = "course")
    public List<TD> tds = new ArrayList<>();
    @ManyToOne
    public Promo promo;


}
