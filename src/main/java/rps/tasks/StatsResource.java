package rps.tasks;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import rps.tasks.model.Student;

@Singleton
@Startup
@Path("/stats")
public class StatsResource {
    @EJB(beanName = "StudentRepository")
    StudentRepository studentRepo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject readTopper() {
        Student wanted = studentRepo.readTopper();

        if (wanted == null) return null;

        return Json.createObjectBuilder()
                .add("name", wanted.getName())
                .add("id", wanted.getId())
                .build();
    }
}
