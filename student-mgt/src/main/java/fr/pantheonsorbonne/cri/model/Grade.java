package fr.pantheonsorbonne.cri.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Grade extends PanacheEntity {


    public Double grade;

    public String comment;
}
