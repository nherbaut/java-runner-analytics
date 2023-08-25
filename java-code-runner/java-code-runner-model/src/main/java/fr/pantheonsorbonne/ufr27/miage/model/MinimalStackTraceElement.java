package fr.pantheonsorbonne.ufr27.miage.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MinimalStackTraceElement(@JsonProperty("className") String className,@JsonProperty("methodName") String methodName, @JsonProperty("lineNumber") int lineNumber) {
}
