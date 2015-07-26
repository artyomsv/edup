package lv.company.edup.resources.secured.reports;

import lv.company.edup.infrastructure.exceptions.InternalException;
import lv.company.edup.infrastructure.exceptions.NotFoundException;
import lv.company.edup.resources.ApplicationFacade;
import lv.company.edup.services.reports.ReportsService;
import lv.company.edup.services.subjects.SubjectsService;
import lv.company.edup.services.subjects.dto.SubjectDto;
import org.apache.commons.lang3.time.DateUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.util.logging.Logger;

@ApplicationScoped
public class ReportsFacade extends ApplicationFacade {

    private Logger logger = Logger.getLogger(ReportsFacade.class.getSimpleName());

    @Inject SubjectsService subjectsService;
    @Inject ReportsService reportsService;

    public Response getVisitingPlanJournal(Long subjectId, String from, String to) {
        SubjectDto subjectDto = subjectsService.find(subjectId);
        if (subjectDto == null) {
            throw new NotFoundException("Subject not found");
        }

        byte[] data = new byte[0];
        try {
            data = reportsService.renderVisitingJournalPlan(subjectDto, DateUtils.parseDate(from, "ddMMyyyy"), DateUtils.parseDate(to, "ddMMyyyy"));
        } catch (ParseException e) {
            throw new InternalException(e);
        }

        return streamResponse(data, "application/pdf", "report.pdf");
    }

}
