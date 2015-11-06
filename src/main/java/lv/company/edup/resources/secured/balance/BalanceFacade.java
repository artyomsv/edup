package lv.company.edup.resources.secured.balance;

import lv.company.edup.infrastructure.exceptions.NotFoundException;
import lv.company.edup.infrastructure.validation.Validated;
import lv.company.edup.persistence.students.Student;
import lv.company.edup.resources.ApplicationFacade;
import lv.company.edup.services.balance.TransactionTypeService;
import lv.company.edup.services.documents.DocumentsService;
import lv.company.edup.services.students.StudentsService;
import lv.company.edup.services.balance.TransactionService;
import lv.company.edup.services.balance.dto.StudentBalanceDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class BalanceFacade extends ApplicationFacade {

    @Inject TransactionService transactionService;
    @Inject TransactionTypeService transactionTypeService;
    @Inject StudentsService studentsService;
    @Inject DocumentsService documentsService;

    public Response search() {
        return ok(transactionService.search());
    }

    @Validated
    public Response saveBalance(@Valid StudentBalanceDto dto) {
        Student student = studentsService.fetchLeanStudent(dto.getStudentId());
        if (student == null) {
            throw new NotFoundException("Missing student id");
        }

        Long save = transactionService.performTransaction(dto, true);
        if (dto.getCash()) {
            documentsService.addDocument(student, dto.getAmount(), dto.getComments());
        }

        return created(save);
    }

    public Response getTransactionTypes() {
        return ok(transactionTypeService.getTransactionTypes());
    }
}
