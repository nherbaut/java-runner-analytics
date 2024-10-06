package fr.pantheonsorbonne.ufr27.miage.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record SnippetSummaryDTO(String title, Long id) {
}
