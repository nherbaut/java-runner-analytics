package fr.pantheonsorbonne.cri.dto;

import fr.pantheonsorbonne.cri.model.Student;

public record StudentSessionDTO(Student student, String attendance, Double grade, String comment) {
    public String getSanitizedStudentName() {
        return this.student.name.replaceAll("\\.", "_").replaceAll("@", "_");
    }
}
