package lv.company.edup.resources.secured.balance;

import lv.company.edup.infrastructure.exceptions.NotFoundException;
import lv.company.edup.persistence.students.Student;
import lv.company.edup.resources.ApplicationFacade;
import lv.company.edup.services.documents.DocumentsService;
import lv.company.edup.services.students.StudentsService;
import lv.company.edup.services.students.TransactionService;
import lv.company.edup.services.students.dto.StudentBalanceDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class BalanceFacade extends ApplicationFacade {

    @Inject TransactionService transactionService;
    @Inject StudentsService studentsService;
    @Inject DocumentsService documentsService;

    public Response search() {
        return ok(transactionService.search());
    }

    public Response saveBalance(StudentBalanceDto dto) {
        Student student = studentsService.fetchLeanStudent(dto.getStudentId());
        if (student == null) {
            throw new NotFoundException("Missing student id");
        }

        Long save = transactionService.debitTransaction(dto);
        if (dto.getCash()) {
            documentsService.addDocument(student, dto.getAmount(), dto.getComments());
        }

        return created(save);
    }

}
