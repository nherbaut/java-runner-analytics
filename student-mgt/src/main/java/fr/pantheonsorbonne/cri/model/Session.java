package fr.pantheonsorbonne.cri.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
public class Session extends PanacheEntity {

    public LocalDate date;

    public LocalDate getDate() {
        return date;
    }


}
