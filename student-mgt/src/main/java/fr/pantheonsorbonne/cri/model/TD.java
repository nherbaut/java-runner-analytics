package fr.pantheonsorbonne.cri.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.util.*;

@Entity
public class TD extends PanacheEntity {
    public String name;

    @ManyToOne
    public Course course;

    @OneToMany
    public Set<Student> students = new HashSet<>();



    @ManyToMany
    public Set<Session> sessions = new HashSet<>();

    public String getShortName() {
        return this.name.substring(this.course.name.length() + this.course.promo.name.length());
    }

    public List<Session> getSessions() {
        List<Session> sessions = new ArrayList<>(this.sessions);
        sessions.sort(Comparator.comparing(s -> s.date));
        return sessions;
    }


}
