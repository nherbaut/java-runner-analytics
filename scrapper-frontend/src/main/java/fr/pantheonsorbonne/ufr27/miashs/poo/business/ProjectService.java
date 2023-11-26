package fr.pantheonsorbonne.ufr27.miashs.poo.business;

import fr.pantheonsorbonne.ufr27.miashs.poo.dao.ProjetDAO;
import fr.pantheonsorbonne.ufr27.miashs.poo.entities.Projet;
import fr.pantheonsorbonne.ufr27.miashs.poo.model.AssignmentForm;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@ApplicationScoped
public class ProjectService {

    @Inject
    ProjetDAO projetDAO;

    public String getProjectOwnerInfo(String url) throws URISyntaxException {

        Optional<Projet> ownerProject = projetDAO.getProjectsForThisWebsite(new URI(url).getHost()).stream().sorted((p0, p1) -> p0.getCreationTime().compareTo(p1.getCreationTime())).findFirst();
        if (ownerProject.isEmpty()) {
            return null;
        } else {
            return new String(Base64.getEncoder().encode((ownerProject.get().getNames() + " " + ownerProject.get().getGroups() + " " + ownerProject.get().getCreationTime()).getBytes(StandardCharsets.UTF_8)));
        }


    }

    public void bookProject(AssignmentForm assignmentForm) throws URISyntaxException, IOException {
        projetDAO.registerProject(assignmentForm);
    }
}
