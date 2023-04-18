package rps.tasks;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import rps.tasks.model.Student;

@Stateless
@Path("/students")
public class StudentResource {
    @EJB(beanName = "StudentRepository")
    StudentRepository repo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Student create(
            @QueryParam("id") Long id,
            @QueryParam("name") String name,
            @QueryParam("semester") Integer semester,
            @QueryParam("cgpa") Double cgpa
    ) {
        return repo.create(id, name, semester, cgpa);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String read(@PathParam("id") Long id) {
        Student s = repo.read(id);

        return s == null ? null : s.getName();
    }

    @GET
    @Path("/compare")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject compare(
            @QueryParam("id1") Long id1,
            @QueryParam("id2") Long id2
    ) {
        Student s1 = repo.read(id1);
        Student s2 = repo.read(id2);

        if (s1 == null || s2 == null) return null;

        Student wanted = s1.getCgpa() > s2.getCgpa() ? s1 : s2;

        return Json.createObjectBuilder()
                .add("name", wanted.getName())
                .add("id", wanted.getId())
                .add("cgpa", wanted.getCgpa())
                .build();
    }

    @GET
    @Path("/rename")
    @Produces(MediaType.APPLICATION_JSON)
    public Student rename(
            @QueryParam("id") Long id,
            @QueryParam("name") String name
    ) {
        Student s = repo.read(id);
        s.setName(name);
        repo.update(s);

        return s;
    }
}
