package lv.company.edup.resources.exposed.documents;

import com.github.javafaker.Faker;
import com.lowagie.text.DocumentException;
import lv.company.edup.infrastructure.pdf.api.DocumentsGenerationService;
import lv.company.edup.infrastructure.templates.api.Template;
import lv.company.edup.infrastructure.templates.api.TemplateEngine;
import lv.company.edup.infrastructure.templates.api.TemplateName;
import lv.company.edup.infrastructure.templates.api.VelocityEngine;
import lv.company.edup.infrastructure.templates.impl.TemplateCache;
import lv.company.edup.infrastructure.templates.impl.templates.VisitorJournalContextCreator;
import lv.company.edup.infrastructure.templates.impl.templates.dto.StudentDto;
import lv.company.edup.infrastructure.templates.impl.templates.dto.VisitingJournalDto;
import lv.company.edup.resources.ApplicationFacade;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class DocumentsFacade extends ApplicationFacade {

    private Logger logger = Logger.getLogger(DocumentsFacade.class.getSimpleName());

    @Inject TemplateCache cache;
    @Inject VisitorJournalContextCreator contextCreator;
    @Inject @VelocityEngine TemplateEngine engine;
    @Inject DocumentsGenerationService service;

    public Response streamDocument() {
        Faker faker = new Faker();
        VisitingJournalDto from = new VisitingJournalDto();
        from.setSubject("Jaunais gads un Ziemassvētki vesturiskā ieskatā");
        from.setDate("22/01/2015");
        from.setTime("10:50");
        for (int i = 0; i < 25; i++) {
            from.getStudents().add(new StudentDto(String.valueOf(i + 1), faker.name().fullName()));
        }
        Map<String, Object> context = contextCreator.create(from);

        Template template = cache.getTemplate(TemplateName.VisitorJournal);

        String render = engine.render(template, context);
        try {
            byte[] data = service.generate(render);
            return streamResponse(data, "application/pdf");
        } catch (IOException | DocumentException e) {
            logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
