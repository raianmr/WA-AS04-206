package rps.tasks;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import rps.tasks.model.Student;

@Singleton
@Startup
public class StudentRepository {
    private EntityManagerFactory factory;
    private EntityManager manager;

    @PostConstruct
    public void initialize() {
        this.factory = Persistence.createEntityManagerFactory("rps_pu");
        this.manager = this.factory.createEntityManager();
    }

    public Student create(Student s) {
        manager.getTransaction().begin();
        manager.persist(s);
        manager.getTransaction().commit();

        return s;
    }

    public Student create(Long id, String name, Integer semester, Double cgpa) {
        Student s = Student.from(id, name, semester, cgpa);

        manager.getTransaction().begin();
        manager.persist(s);
        manager.getTransaction().commit();

        return s;
    }

    public Student read(Long id) {
        return manager.find(Student.class, id);
    }

    public Student update(Student s) {
        Student toUpdate = manager.find(Student.class, s.getId());

        manager.getTransaction().begin();
        toUpdate.setName(s.getName());
        toUpdate.setSemester(s.getSemester());
        toUpdate.setCgpa(s.getCgpa());
        manager.getTransaction().commit();

        return toUpdate;
    }

    public void delete(Student s) {
        manager.getTransaction().begin();
        manager.remove(s);
        manager.getTransaction().commit();
    }

    @PreDestroy
    public void close() {
        this.manager.close();
        this.factory.close();
    }

    public Student readTopper() {
        TypedQuery<Student> query = manager.createQuery(
                "SELECT s FROM Student s WHERE s.id = " +
                "(SELECT max(s.id) FROM Student s)", Student.class);

        return query.getSingleResult();
    }
}
