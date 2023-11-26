package fr.pantheonsorbonne.ufr27.miashs.poo.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.pantheonsorbonne.ufr27.miashs.poo.model.AssignmentForm;
import jakarta.persistence.*;

import java.io.IOException;
import java.io.StringWriter;
import java.time.Instant;
import java.util.UUID;

@Entity
public class Projet {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    public Instant getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Instant creationTime) {
        this.creationTime = creationTime;
    }

    private Instant creationTime;

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAddressAssignment() {
        return addressAssignment;
    }

    public void setAddressAssignment(String addressAssignment) {
        this.addressAssignment = addressAssignment;
    }

    public String getRealAssignment() {
        return realAssignment;
    }

    public void setRealAssignment(String realAssignment) {
        this.realAssignment = realAssignment;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    private String website;
    @Column(length = 1024)
    private String addressAssignment;
    @Column(length = 1024)
    private String realAssignment;
    @Column(nullable = false)
    private String names;
    @Column(nullable = false)
    private String groups;

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public String getAssignmentFormJSON() {
        return assignmentFormJSON;
    }

    public AssignmentForm getAssignmentForm() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(assignmentFormJSON.getBytes(), AssignmentForm.class);

    }

    public void setAssignmentFormJSON(String assignmentFormJSON) {
        this.assignmentFormJSON = assignmentFormJSON;
    }

    public void setAssignmentFormJSON(AssignmentForm assignmentForm) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringWriter sw = new StringWriter();
        mapper.writeValue(sw, assignmentForm);
        this.assignmentFormJSON = sw.toString();
    }

    @Lob
    @Column(length = 10000)
    private String assignmentFormJSON;
}
