package fr.pantheonsorbonne.cri.service;

import com.unboundid.ldap.sdk.*;
import fr.pantheonsorbonne.cri.model.*;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class Setup {

    @Transactional
    public void onStart(@Observes StartupEvent event) {



    }


}
