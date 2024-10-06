package fr.pantheonsorbonne.cri.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record GradeDTO(Double grade, String comment) {
}
