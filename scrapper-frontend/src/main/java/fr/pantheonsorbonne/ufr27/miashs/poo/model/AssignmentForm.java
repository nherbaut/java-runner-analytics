package fr.pantheonsorbonne.ufr27.miashs.poo.model;

import java.util.List;

public record AssignmentForm(String assignementURL, List<FormData> formData, String groups, String names) {
    public AssignmentForm(String assignementURL, List<FormData> formData) {
        this(assignementURL, formData, "", "");
    }
}
