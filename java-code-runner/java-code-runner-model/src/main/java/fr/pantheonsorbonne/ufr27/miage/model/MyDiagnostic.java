package fr.pantheonsorbonne.ufr27.miage.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Objects;

public class MyDiagnostic {


    private String source;
    private String messageEN;
    private String code;
    private Long position;
    private Long startPosition;
    private Long endPosition;
    private Long lineNumber;
    private Long columnNumber;
    private String messageFR;

    private String kind;

    public static MyDiagnostic fromDiagnosis(Diagnostic diag) {
        return new MyDiagnostic(diag.getMessage(Locale.ENGLISH), diag.getCode(), diag.getPosition(), diag.getStartPosition(), diag.getEndPosition(), diag.getLineNumber(), diag.getColumnNumber(), diag.getMessage(Locale.FRENCH), Paths.get(((JavaFileObject) (diag.getSource())).getName()).getFileName().toString(), diag.getKind().name());
    }

    public MyDiagnostic() {
    }

    public MyDiagnostic(String messageEN, String code, Long position, Long startPosition, Long endPosition, Long lineNumber, Long columnNumber, String messageFR, String source, String kind) {
        this.messageEN = messageEN;
        this.code = code;
        this.position = position!=Diagnostic.NOPOS?position:0;
        this.startPosition = startPosition!=Diagnostic.NOPOS?startPosition:0;
        this.endPosition = endPosition!=Diagnostic.NOPOS?endPos ition:0;
        this.lineNumber = lineNumber!=Diagnostic.NOPOS?lineNumber:0;
        this.columnNumber = columnNumber!=Diagnostic.NOPOS?columnNumber:0;
        this.messageFR = messageFR;
        this.source = source;
        this.kind = kind;
    }

    public MyDiagnostic(String message, String source) {
        this(message, "", null, null, null, null, null, message, source, null);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getKind() {
        return this.kind;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getSource() {
        return this.source;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Long getPosition() {
        return this.position;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Long getStartPosition() {
        return this.startPosition;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Long getEndPosition() {
        return this.endPosition;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Long getLineNumber() {
        return this.lineNumber;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Long getColumnNumber() {
        return this.columnNumber;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)

    public String getCode() {
        return this.code;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getMessageFR() {
        return this.messageFR;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getMessageEN() {
        return this.messageEN;
    }

    public void setMessageEN(String messageEN) {
        this.messageEN = messageEN;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public void setStartPosition(Long startPosition) {
        this.startPosition = startPosition;
    }

    public void setEndPosition(Long endPosition) {
        this.endPosition = endPosition;
    }

    public void setLineNumber(Long lineNumber) {
        this.lineNumber = lineNumber;
    }

    public void setColumnNumber(Long columnNumber) {
        this.columnNumber = columnNumber;
    }

    public void setMessageFR(String messageFR) {
        this.messageFR = messageFR;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MyDiagnostic that)) return false;
        return Objects.equals(getSource(), that.getSource()) && Objects.equals(getMessageEN(), that.getMessageEN()) && Objects.equals(getCode(), that.getCode()) && Objects.equals(getPosition(), that.getPosition()) && Objects.equals(getStartPosition(), that.getStartPosition()) && Objects.equals(getEndPosition(), that.getEndPosition()) && Objects.equals(getLineNumber(), that.getLineNumber()) && Objects.equals(getColumnNumber(), that.getColumnNumber()) && Objects.equals(getMessageFR(), that.getMessageFR()) && Objects.equals(getKind(), that.getKind());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSource(), getMessageEN(), getCode(), getPosition(), getStartPosition(), getEndPosition(), getLineNumber(), getColumnNumber(), getMessageFR(), getKind());
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
}
