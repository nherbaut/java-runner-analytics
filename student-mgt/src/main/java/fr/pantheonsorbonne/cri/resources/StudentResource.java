package fr.pantheonsorbonne.cri.resources;

import fr.pantheonsorbonne.cri.dto.GradeDTO;
import fr.pantheonsorbonne.cri.dto.StudentSessionDTO;
import fr.pantheonsorbonne.cri.model.*;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;
import io.smallrye.common.annotation.Blocking;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.time.LocalDate;
import java.util.*;

@Path("/students")
@Authenticated
public class StudentResource {

    @Inject
    Template index;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance student(Student student);
    }

    @GET
    @Path("{studentName}")
    @Produces(MediaType.TEXT_HTML)
    @RolesAllowed("admin-java")
    @Blocking
    public TemplateInstance get(@PathParam("studentName") String studentName) {
        Student stu = Student.find("name", studentName).firstResult();
        return Templates.student(stu);

    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("me")
    @Blocking
    public TemplateInstance getMe(@Context SecurityContext context) throws Exception {
        Student stu = Student.find("name", context.getUserPrincipal().getName()).firstResult();
        if (Objects.isNull(stu)) {
            throw new WebApplicationException(404);
        }
        return Templates.student(stu);
    }

    @GET
    @Path("{promo}/{course}/{td}/{date}")
    @RolesAllowed("admin-java")
    @Produces(MediaType.TEXT_HTML)
    @Blocking
    public TemplateInstance get(@PathParam("promo") String promo, @PathParam("course") String course, @PathParam("td") String td, @PathParam("date") String localDate) {
        List<SessionResults> sr = SessionResults.find("select sr from SessionResults sr where sr.td.name=?1 and sr.session.date=?2", promo + course + td, LocalDate.parse(localDate)).list();
        List<StudentSessionDTO> dto = sr.stream().sorted(Comparator.comparing(sr2 -> sr2.student)).map(
                s -> new StudentSessionDTO(s.student, s.attendance != null ? s.attendance.name() : "", Objects.requireNonNullElse(s.grade, new Grade()).grade, Objects.requireNonNullElse(s.grade, new Grade()).comment)
        ).toList();
        return index.data("sessions", dto, "promo", promo, "course", course, "td", td, "date", localDate);
    }

    @Path("{promo}/{course}/{td}")
    @RolesAllowed("admin-java")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Set<Student> students(@PathParam("promo") String promo, @PathParam("course") String course, @PathParam("td") String td) {

        return getStudents(promo, course, td);
    }

    @Path("{promo}/{course}/{td}/{date}/{name}")
    @RolesAllowed("admin-java")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public SessionResults getSessionResultForStudent(@PathParam("promo") String promo, @PathParam("course") String course, @PathParam("td") String td, @PathParam("date") String date, @PathParam("name") String name) {

        return SessionResults.find("select sr from SessionResults sr where sr.student.name=?1 and sr.td.name=?2 and sr.session.date=?3", name, promo + course + td, LocalDate.parse(date)).project(SessionResults.class).stream().findAny().orElseThrow(() -> new WebApplicationException(404));
    }

    private static Set<Student> getStudents(String promo, String course, String td) {
        return ((TD) TD.find("name", promo + course + td).list().getFirst()).students;
    }


    @Transactional
    @Path("{promo}/{course}/{td}/{name}/{date}/attendance/{attendance}")
    @RolesAllowed("admin-java")
    @POST
    public Response postAttendanceStudent(@PathParam("promo") String promo, @PathParam("course") String course, @PathParam("td") String td, @PathParam("name") String name, @PathParam("attendance") Attendance attendance, @PathParam("date") String localDate) {
        Student student = Student.find("name", name).firstResult();
        TD atd = TD.find("name", promo + course + td).firstResult();
        if (atd.students.contains(student)) {
            LocalDate sessionDate = LocalDate.parse(localDate);
            Optional<SessionResults> sessionResults = student.sessionResults.stream().filter(sr -> sr.session.date.equals(sessionDate) && sr.td.equals(atd)).findAny();
            if (sessionResults.isPresent()) {
                sessionResults.get().attendance = attendance;
                return Response.noContent().build();
            }
        }


        throw new WebApplicationException(404);


    }

    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{promo}/{course}/{td}/{name}/{date}/grade")
    @RolesAllowed("admin-java")
    @POST
    public Response postGradeStudent(@PathParam("promo") String promo, @PathParam("course") String course, @PathParam("td") String td, @PathParam("name") String name, GradeDTO grade, @PathParam("date") String localDate) {
        Student student = Student.find("name", name).firstResult();
        TD atd = TD.find("name", promo + course + td).firstResult();
        if (atd.students.contains(student)) {
            LocalDate sessionDate = LocalDate.parse(localDate);
            Optional<SessionResults> sessionResults = student.sessionResults.stream().filter(sr -> sr.session.date.equals(sessionDate) && sr.td.equals(atd)).findAny();
            if (sessionResults.isPresent()) {
                Grade aGrade = new Grade();
                aGrade.grade = grade.grade();
                aGrade.comment = grade.comment();
                aGrade.persist();
                sessionResults.get().grade = aGrade;

                return Response.noContent().build();
            }
        }


        throw new WebApplicationException(404);


    }
}
