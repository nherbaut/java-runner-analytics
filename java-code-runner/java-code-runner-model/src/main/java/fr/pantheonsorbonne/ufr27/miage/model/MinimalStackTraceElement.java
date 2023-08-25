package fr.pantheonsorbonne.ufr27.miage.model;

<<<<<<< HEAD
public record MinimalStackTraceElement(String className, String methodName, int lineNumber) {
=======
import com.fasterxml.jackson.annotation.JsonProperty;

public record MinimalStackTraceElement(@JsonProperty("className") String className,@JsonProperty("methodName") String methodName, @JsonProperty("lineNumber") int lineNumber) {
>>>>>>> 5dc7f83 (java-code-runner with submodules)
}
