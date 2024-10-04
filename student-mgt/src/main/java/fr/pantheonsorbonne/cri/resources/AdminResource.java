package fr.pantheonsorbonne.cri.resources;

import com.unboundid.ldap.sdk.*;
import fr.pantheonsorbonne.cri.model.*;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Path("/admin")
@Authenticated

public class AdminResource {
    private static final Logger LOG = LoggerFactory.getLogger(AdminResource.class);

    @Transactional
    public static void createSessions(LocalDate startDate, LocalDate endDate) {


        // Adjust start date to the next Monday if it isn't already a Monday
        if (startDate.getDayOfWeek() != DayOfWeek.MONDAY) {
            startDate = startDate.with(DayOfWeek.MONDAY);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Loop through each Monday until the end date
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusWeeks(1)) {
            Session session = new Session();

            session.date = date;
            session.persistAndFlush();

        }
    }

    @Transactional
    public static Promo createPromo(String promoName, Course... courses) {
        Promo promo = new Promo();
        promo.name = promoName;
        promo.courses.addAll(Arrays.asList(courses));
        promo.persistAndFlush();
        return promo;
    }

    @Transactional
    public static Course createCourse(String courseName, TD... tds) {
        Course course = new Course();
        course.name = courseName;

        course.persistAndFlush();
        Arrays.stream(tds).forEach(td -> td.course = course);
        return course;
    }

    @Transactional
    public static TD createTD(String tdName) {
        TD td = new TD();
        td.name = tdName;
        td.persistAndFlush();
        return td;
    }

    @Transactional
    @GET
    @RolesAllowed("admin-java")
    public void createTables() {
        {
            TD l2pootd1 = createTD("l2pootd1");
            TD l2pootd2 = createTD("l2pootd2");
            TD l2pootd3 = createTD("l2pootd3");

            TD m1inf2gr1 = createTD("m1inf2gr1");
            TD m1inf2gr2 = createTD("m1inf2gr2");
            TD l3inf2gr1 = createTD("l3inf2gr1");
            Course m1inf2 = createCourse("inf2", m1inf2gr1, m1inf2gr2);
            Course l3inf2 = createCourse("inf2", l3inf2gr1);
            Course l2poo = createCourse("poo", l2pootd1, l2pootd2, l2pootd3);

            Promo m1 = createPromo("m1", m1inf2);
            Promo l3 = createPromo("l3", l3inf2);
            Promo l2 = createPromo("l2", l2poo);
            m1inf2.promo = m1;
            l3inf2.promo = l3;
            l2poo.promo = l2;


            createSessions(LocalDate.of(2024, 9, 17),
                    LocalDate.of(2024, 12, 31));

        }
    }

    @GET
    @Path("ldap")
    @Transactional
    public void syncLdap() {


        String ldapHost = "10.0.0.1";
        int ldapPort = 3890;
        String bindDN = "uid=bind_user,ou=people,dc=miage,dc=dev";
        String bindPassword = "j6QDThPlx4ONpTHxRpWTYPAPwLLXjYKk27TlbyLavtiC1TX26gX0VO0rsmpkx6z";
        String searchBase = "dc=miage,dc=dev";


        LDAPConnection connection = null;

        try {
            // Establish LDAP connection
            connection = new LDAPConnection(ldapHost, ldapPort, bindDN, bindPassword);
            System.out.println("Connected to LDAP server");
            List<Promo> promos = Promo.findAll().list();
            List<Session> sessions = Session.listAll();
            for (Promo promo : promos) {
                for (Course course : promo.courses) {
                    for (TD td : course.tds) {


                        syncTD(td, searchBase, connection, sessions);


                    }
                }
            }


        } catch (LDAPException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

    }

    @Transactional
    public static void syncTD(TD td, String searchBase, LDAPConnection connection, List<Session> sessions) throws LDAPSearchException {
        // Create a filter to search for users belonging to group 'l2'
        Filter filter = Filter.createEqualityFilter("memberOf", "cn=" + td.name + ",ou=groups," + searchBase);

        // Perform the search
        SearchResult searchResult = connection.search(searchBase, SearchScope.SUB, filter);
        td.sessions.addAll(sessions);

        // Iterate over the search results and print user details
        for (SearchResultEntry entry : searchResult.getSearchEntries()) {

            String uid = entry.getAttributeValue("uid");
            LOG.info("working on student {}", uid);
            Student student;
            Optional<PanacheEntityBase> dbstudent = Student.find("name", uid).stream().findAny();
            if (dbstudent.isEmpty()) {
                LOG.info("working on student {} creating new", uid);
                student = new Student();
                student.name = entry.getAttributeValue("uid");
                student.email = entry.getAttributeValue("mail");
                student.persistAndFlush();
            } else {
                LOG.info("working on student {} existing", uid);
                student = (Student) dbstudent.get();

            }
            LOG.info("working on student {} added to td {}", uid, td.name);
            td.students.add(student);


            for (Session session : sessions) {
                LOG.info("working on student {} td {} looking for session {}", uid, td.name, session.date);
                List<SessionResults> studentSRTd = student.sessionResults.stream().filter(sr -> sr.td == td).toList();
                if (studentSRTd.stream().filter(sr -> sr.session.date.equals(session.date)).findAny().isEmpty()) {
                    LOG.info("working on student {} td {} looking for session {} creating new", uid, td.name, session.date);
                    SessionResults sessionResults = new SessionResults();
                    sessionResults.session = session;
                    sessionResults.td = td;
                    sessionResults.student = student;
                    sessionResults.persistAndFlush();
                }


            }
        }
    }
}
