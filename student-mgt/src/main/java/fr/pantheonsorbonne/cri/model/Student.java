package fr.pantheonsorbonne.cri.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;

@Entity
public class Student extends PanacheEntity implements Comparable<Student> {
    public String name;
    public String email;


    @OneToMany(mappedBy = "student")
    public List<SessionResults> sessionResults = new ArrayList<>();

    public Double getAverageScore() {
        OptionalDouble od = this.sessionResults.stream().mapToDouble(sr -> sr.grade.grade).average();
        if (od.isPresent()) {
            return od.getAsDouble();
        } else {
            return null;
        }
    }

    @Override
    public int compareTo(@Nonnull Student o) {
        String me = this.name;
        if (me.split("\\.").length == 2) {
            me = me.split("\\.")[1] + me.split("\\.")[0];
        }
        String him = o.name;
        if (him.split("\\.").length == 2) {
            him = him.split("\\.")[1] + him.split("\\.")[0];
        }

        return me.compareTo(him);
    }
}
