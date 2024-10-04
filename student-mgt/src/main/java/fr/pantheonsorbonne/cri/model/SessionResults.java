package fr.pantheonsorbonne.cri.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class SessionResults extends PanacheEntity {


    public Attendance attendance;
    @ManyToOne
    public Grade grade;
    @ManyToOne
    public Session session;
    @JsonIgnore
    @ManyToOne
    public Student student;
    @JsonIgnore
    @ManyToOne
    public TD td;
}
