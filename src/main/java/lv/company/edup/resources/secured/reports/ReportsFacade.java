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
import java.util.Date;
import java.util.logging.Logger;

@ApplicationScoped
public class ReportsFacade extends ApplicationFacade {

    private Logger logger = Logger.getLogger(ReportsFacade.class.getSimpleName());

    @Inject SubjectsService subjectsService;
    @Inject ReportsService reportsService;

    public Response getVisitingPlanJournal(Long subjectId, String from, String to, boolean withAttendance) {
        SubjectDto subjectDto = subjectsService.find(subjectId);
        if (subjectDto == null) {
            throw new NotFoundException("Subject not found");
        }

        try {
            Date fromDate = from != null ? DateUtils.parseDate(from, "ddMMyyyy") : new Date();
            Date toDate = to != null ? DateUtils.parseDate(to, "ddMMyyyy") : DateUtils.addYears(new Date(), 100);

            byte[] data = reportsService.renderVisitingJournalPlan(subjectDto, fromDate, toDate, withAttendance);
            return streamResponse(data, "application/pdf", "report.pdf");
        } catch (ParseException e) {
            throw new InternalException(e);
        }

    }

}
