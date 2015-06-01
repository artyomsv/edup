package lv.company.edup.resources.secured.students;

import lv.company.edup.persistence.EntityPayload;
import lv.company.edup.resources.ApplicationFacade;
import lv.company.edup.services.students.StudentsService;
import lv.company.edup.services.students.dto.StudentDto;

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
        return ok(service.search());
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
        EntityPayload payload = service.createStudentVersion(dto);
        service.updateIndex(dto);
        return created(payload.getId());
    }

    public Response updateStudent(StudentDto dto, Long id) {
        EntityPayload payload = service.updateStudent(dto, id);
        service.updateIndex(dto);
        return updated(payload.getVersionId());
    }

    public Response deleteStudent(Long id) {
        logger.log(Level.INFO, "delete student {0} is not supported yet", id);
        return ok();
    }

    public Response fill(int count) {
        return ok(service.fillFakeData(count));
    }

    public Response rebuild() {
        service.rebuild();
        return ok();
    }
}
