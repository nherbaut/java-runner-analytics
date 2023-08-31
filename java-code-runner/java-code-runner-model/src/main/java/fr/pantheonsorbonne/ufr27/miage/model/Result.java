package fr.pantheonsorbonne.ufr27.miage.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Result {
    private static final int STDOUT_LENGHT_LIMIT = 2000;

    public String getStdout() {
        return stdout.toString();
    }

    public void setStdout(String stdout) {
        if(stdout.length()<=STDOUT_LENGHT_LIMIT)
            this.stdout = new StringBuilder(stdout);
        else{
            this.stdout=new StringBuilder(stdout.substring(0,STDOUT_LENGHT_LIMIT));
            this.stdout.append("...<<truncated>>");
        }
    }

    public void appendStdout(String stdout) {
        if (this.stdout.length() <= STDOUT_LENGHT_LIMIT)
            this.stdout.append(stdout);
    }

    StringBuilder stdout = new StringBuilder();

    public Result() {
    }


    public List<RuntimeError> getRuntimeError() {
        return runtimeError;
    }

    List<RuntimeError> runtimeError = new ArrayList<>();


    public List<MyDiagnostic> getCompilationDiagnostic() {
        return compilationDiagnostic;
    }

    List<MyDiagnostic> compilationDiagnostic = new ArrayList<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Result result)) return false;
        return Objects.equals(getStdout(), result.getStdout()) && Objects.equals(getRuntimeError(), result.getRuntimeError()) && Objects.equals(getCompilationDiagnostic(), result.getCompilationDiagnostic());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStdout(), getRuntimeError(), getCompilationDiagnostic());
    }

    @Override
    public String toString() {
        return "Result{" +
                "stdout=" + stdout +
                ", runtimeError=" + runtimeError +
                ", compilationDiagnostic=" + compilationDiagnostic +
                '}';
    }

    public void setRuntimeError(List<RuntimeError> runtimeError) {
        this.runtimeError = runtimeError;
    }

    public void setCompilationDiagnostic(List<MyDiagnostic> compilationDiagnostic) {
        this.compilationDiagnostic = compilationDiagnostic;
    }
}
