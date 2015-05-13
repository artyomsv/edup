package lv.company.edup.infrastructure.templates.impl;

import com.github.javafaker.Faker;
import lv.company.edup.infrastructure.pdf.impl.PDFDocumentsGenerationServiceImpl;
import lv.company.edup.infrastructure.templates.api.Template;
import lv.company.edup.infrastructure.templates.api.TemplateName;
import lv.company.edup.infrastructure.templates.impl.templates.VisitorJournalContextCreator;
import lv.company.edup.infrastructure.templates.impl.templates.dto.StudentDto;
import lv.company.edup.infrastructure.templates.impl.templates.dto.VisitingJournalDto;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.util.Map;
import java.util.logging.Logger;

@RunWith(MockitoJUnitRunner.class)
public class TemplateCacheTest {

    @Mock private Logger logger;
    @InjectMocks private TemplateCache cache;
    @InjectMocks private VisitorJournalContextCreator contextCreator;
    @InjectMocks private VelocityTemplateEngine engine;
    @InjectMocks private PDFDocumentsGenerationServiceImpl service;

    @Before
    public void setUp() throws Exception {
        cache.init();
    }

    @Test
    public void testName() throws Exception {
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

        engine.init();
        String render = engine.render(template, context);
        byte[] generate = service.generate(render);

        IOUtils.write(render, FileUtils.openOutputStream(new File("test.html")));
        IOUtils.write(generate, FileUtils.openOutputStream(new File("test.pdf")));

    }
}