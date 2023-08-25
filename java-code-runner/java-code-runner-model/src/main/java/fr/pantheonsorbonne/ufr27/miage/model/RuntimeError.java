package fr.pantheonsorbonne.ufr27.miage.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class RuntimeError {
    String message;

    public RuntimeError() {
    }

    public void setStackTraceElements(List<MinimalStackTraceElement> stackTraceElements) {
        this.stackTraceElements = stackTraceElements;
    }

    List<MinimalStackTraceElement> stackTraceElements=new ArrayList<>();

    public RuntimeError(String message) {
        this.message = message;
    }

    public RuntimeError(String message, Collection<MinimalStackTraceElement> stackTraceElements) {
        this.message = message;
        this.stackTraceElements.addAll(stackTraceElements);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<MinimalStackTraceElement> getStackTraceElements() {
        return stackTraceElements;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RuntimeError that)) return false;
        return Objects.equals(getMessage(), that.getMessage()) && Objects.equals(getStackTraceElements(), that.getStackTraceElements());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMessage(), getStackTraceElements());
    }

    @Override
    public String toString() {
        return "RuntimeError{" +
                "message='" + message + '\'' +
                ", stackTraceElements=" + stackTraceElements +
                '}';
    }
}
