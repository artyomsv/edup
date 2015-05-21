package lv.company.edup.resources.secured.students;

import lv.company.edup.infrastructure.templates.impl.templates.dto.StudentTemplateData;
import lv.company.edup.resources.ApplicationFacade;
import lv.company.edup.services.students.StudentDto;
import lv.company.edup.services.students.StudentsService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class StudentsFacade extends ApplicationFacade {

    private final Logger logger = Logger.getLogger(StudentsFacade.class.getSimpleName());
    @Inject StudentsService service;

    public Response search() {
        return ok(service.findAll());
    }

    public Response findStudent(Long id) {
        return ok(service.findStudent(id));
    }

    public Response findVersions(Long id) {
        return ok(service.findVersions(id));
    }

    public Response findVersion(Long id, Long versionId) {
        return ok(service.findVersion(id, versionId));
    }

    public Response createStudent(StudentDto dto) {
        Long id = service.createdStudent(dto);
        return created(id);
    }

    public Response updateStudent(StudentTemplateData dto, Long id) {
        logger.log(Level.INFO, "put student {0}", id);
        return ok();
    }

    public Response deleteStudent(Long id) {
        logger.log(Level.INFO, "delete student {0}", id);
        return ok();
    }
}
