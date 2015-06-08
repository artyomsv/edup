package lv.company.edup.resources.secured.students;

import lv.company.edup.infrastructure.exceptions.BadRequestException;
import lv.company.edup.infrastructure.exceptions.NotFoundException;
import lv.company.edup.persistence.EntityPayload;
import lv.company.edup.resources.ApplicationFacade;
import lv.company.edup.services.students.BalanceService;
import lv.company.edup.services.students.StudentsService;
import lv.company.edup.services.students.dto.CurrentBalanceDto;
import lv.company.edup.services.students.dto.StudentDto;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class StudentsFacade extends ApplicationFacade {

    private final Logger logger = Logger.getLogger(StudentsFacade.class.getSimpleName());

    @Inject StudentsService studentsService;
    @Inject BalanceService balanceService;

    public Response search() {
        return ok(studentsService.search());
    }

    public Response findStudent(Long id) {
        StudentDto student = studentsService.findStudent(id);
        fetchStudentBalance(id, student);
        return ok(student);
    }

    public Response findVersions(Long id) {
        return ok(studentsService.findVersions(id));
    }

    public Response findVersion(Long id, Long versionId) {
        return ok(studentsService.findVersion(id, versionId));
    }

    public Response createStudent(StudentDto dto) {
        EntityPayload payload = studentsService.createStudentVersion(dto);
        studentsService.updateIndex(payload.getId());
        return created(payload.getId(), payload.getVersionId());
    }

    public Response updateStudent(StudentDto dto, Long id, String etag) {
        if (StringUtils.isBlank(etag)) {
            throw new BadRequestException("Missing etag");
        }

        EntityPayload payload = studentsService.updateStudent(dto, id, etag);
        studentsService.updateIndex(payload.getId());
        return updated(payload.getVersionId());
    }

    public Response deleteStudent(Long id) {
        logger.log(Level.INFO, "delete student {0} is not supported yet", id);
        return ok();
    }

    public Response fill(int count) {
        return ok(studentsService.fillFakeData(count));
    }

    public Response rebuild() {
        studentsService.rebuild();
        return ok();
    }

    public Response getStudentBalance(Long id) {
        if (studentsService.fetchLeanStudent(id) == null) {
            throw new NotFoundException("Missing student with [" + id + "] id.");
        }
        return ok(balanceService.currentStudentBalance(id));
    }

    private void fetchStudentBalance(Long id, StudentDto student) {
        if (student != null) {
            CurrentBalanceDto balance = balanceService.currentStudentBalance(id);
            student.setBalance(balance != null ? balance.getAmount() : 0L);
        }
    }
}
