package fr.pantheonsorbonne.ufr27.miage.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record UserDTO(String owner) {
}
