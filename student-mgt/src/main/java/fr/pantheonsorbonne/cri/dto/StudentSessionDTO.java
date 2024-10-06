package fr.pantheonsorbonne.cri.dto;

import fr.pantheonsorbonne.cri.model.Student;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record StudentSessionDTO(Student student, String attendance, Double grade, String comment) {
    public String getSanitizedStudentName() {
        return this.student.name.replaceAll("\\.", "_").replaceAll("@", "_");
    }
}
