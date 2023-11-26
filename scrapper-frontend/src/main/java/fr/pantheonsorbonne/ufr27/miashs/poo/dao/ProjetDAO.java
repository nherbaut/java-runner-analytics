package fr.pantheonsorbonne.ufr27.miashs.poo.dao;

import fr.pantheonsorbonne.ufr27.miashs.poo.entities.Projet;
import fr.pantheonsorbonne.ufr27.miashs.poo.model.AssignmentForm;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProjetDAO {

    @Inject
    EntityManager em;

    public List<Projet> getProjectsForThisWebsite(String website) {
        List<Projet> matchingProjects = em.createQuery("SELECT p from Projet p where p.website=?1").setParameter(1, website).getResultList();
        return matchingProjects;
    }

    public boolean isWebsiteAvailable(String website) {
        return getProjectsForThisWebsite(website).isEmpty();
    }

    public  List<Projet> getDuplicateProject(){
        return null;
        //TODO

    }
    @Transactional
    public void registerProject(AssignmentForm assignmentForm) throws URISyntaxException, IOException {
        Projet projet = new Projet();
        projet.setAddressAssignment(assignmentForm.assignementURL());
        String realURL = new String(Base64.getDecoder().decode(assignmentForm.assignementURL().split("/url/")[1]));
        projet.setRealAssignment(realURL);
        projet.setGroups(assignmentForm.groups());
        projet.setNames(assignmentForm.names());
        projet.setWebsite(new URI(realURL).getHost());
        projet.setCreationTime(Instant.now());
        projet.setAssignmentFormJSON(assignmentForm);
        em.persist(projet);
    }

    public List<Projet> getAllProjects() {
        return em.createQuery("SELECT p from Projet p").getResultList();
    }

    public AssignmentForm getAssignementFormByProjectId(UUID projectID) throws IOException {
        return em.find(Projet.class,projectID).getAssignmentForm();
    }
}
