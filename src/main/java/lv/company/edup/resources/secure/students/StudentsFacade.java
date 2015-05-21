package lv.company.edup.resources.secure.students;

import lv.company.edup.infrastructure.templates.impl.templates.dto.StudentDto;
import lv.company.edup.resources.ApplicationFacade;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class StudentsFacade extends ApplicationFacade {

    private final Logger logger = Logger.getLogger(StudentsFacade.class.getSimpleName());

    public Response search() {
        logger.info("search");
        return ok();
    }

    public Response findStudent(Long id) {
        logger.log(Level.INFO, "get by id {0}", id);
        return ok();
    }

    public Response findVersions(Long id) {
        logger.log(Level.INFO, "get versions by id {0}", id);
        return ok();
    }

    public Response findVersion(Long id, Long versionId) {
        logger.log(Level.INFO, "get version by id {0} and versionId {1}", new String[]{String.valueOf(id), String.valueOf(versionId)});
        return ok();
    }

    public Response createStudent(StudentDto dto) {
        logger.log(Level.INFO, "post student");
        return created(1L);
    }

    public Response updateStudent(StudentDto dto, Long id) {
        logger.log(Level.INFO, "put student {0}", id);
        return ok();
    }

    public Response deleteStudent(Long id) {
        logger.log(Level.INFO, "delete student {0}", id);
        return ok();
    }
}
