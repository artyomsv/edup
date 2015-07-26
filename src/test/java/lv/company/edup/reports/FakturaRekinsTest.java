package lv.company.edup.reports;

import lv.company.edup.infrastructure.templates.api.ContextCreator;
import lv.company.edup.infrastructure.templates.api.TemplateName;
import lv.company.edup.infrastructure.templates.api.Type;
import lv.company.edup.infrastructure.templates.impl.JasperTemplateEngine;
import lv.company.edup.infrastructure.templates.impl.TemplateCache;
import lv.company.edup.infrastructure.templates.impl.VelocityPropertiesProducer;
import lv.company.edup.infrastructure.templates.impl.VelocityTemplateEngine;
import lv.company.edup.infrastructure.templates.impl.templates.EventPlanningJournalContextCreator;
import lv.company.edup.infrastructure.templates.impl.templates.FakturaContextCreator;
import lv.company.edup.infrastructure.templates.impl.templates.dto.EventData;
import lv.company.edup.infrastructure.templates.impl.templates.dto.EventPlanningJournal;
import lv.company.edup.infrastructure.templates.impl.templates.dto.FakturaData;
import lv.company.edup.infrastructure.templates.impl.templates.dto.StudentData;
import lv.company.edup.infrastructure.templates.impl.templates.dto.TimeData;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakturaRekinsTest {

    ContextCreator<FakturaData> fakturaCreator = new FakturaContextCreator();
    ContextCreator<EventPlanningJournal> journalCreator = new EventPlanningJournalContextCreator();

    JasperTemplateEngine jasperEngine;
    VelocityTemplateEngine velocityEngine;
    TemplateCache cache;

    @Before
    public void setUp() throws Exception {
        cache = new TemplateCache();
        cache.init();

        fakturaCreator = new FakturaContextCreator();

        jasperEngine = new JasperTemplateEngine();
        velocityEngine = new VelocityTemplateEngine();
        velocityEngine.setProperties(new VelocityPropertiesProducer().getVelocityProperties());
        velocityEngine.init();
    }

    @After
    public void tearDown() throws Exception {
        cache = null;
        fakturaCreator = null;
        jasperEngine = null;
    }

    @Test
    public void fakturaRekins() throws Exception {
        FakturaData from = new FakturaData();
        from.setPaymentDescription("This is some description");
        from.setPaymentId(1L);
        from.setPaymentTotal(256L);
        Map<String, Object> context = fakturaCreator.create(from);
        FileUtils.writeByteArrayToFile(new File("faktura.pdf"), jasperEngine.render(cache.getTemplate(TemplateName.FakturaJasper), context, Type.PDF));
//        FileUtils.writeByteArrayToFile(new File("faktura.html"), jasperEngine.render(cache.getTemplate(TemplateName.FakturaJasper), context, Type.HTML));
//        FileUtils.writeByteArrayToFile(new File("faktura.xlsX"), jasperEngine.render(cache.getTemplate(TemplateName.FakturaJasper), context, Type.XLSX));
    }

    @Test
    public void testName2() throws Exception {
        List<EventData> events = new ArrayList<>();
        events.add(new EventData(140L, "Date1", Arrays.asList(new TimeData(140L, "Time 1_1"), new TimeData(180L, "Time 1_2"))));
        events.add(new EventData(220L, "Date2", Arrays.asList(new TimeData(220L, "Time 2_1"), new TimeData(260L, "Time 2_2"), new TimeData(300L, "Time 2_3"))));
        events.add(new EventData(340L, "Date3", Arrays.asList(new TimeData(340L, "Time 3_1"), new TimeData(380L, "Time 3_2"), new TimeData(420L, "Time 3_3"))));
        events.add(new EventData(460L, "Date4", Arrays.asList(new TimeData(460L, "Time 4_1"), new TimeData(500L, "Time 4_2"))));
        events.add(new EventData(540L, "Date5", Arrays.asList(new TimeData(540L, "Time 5_1"), new TimeData(580L, "Time 5_2"), new TimeData(620L, "Time 5_3"))));
        events.add(new EventData(660L, "Date6", Arrays.asList(new TimeData(660L, "Time 6_1"), new TimeData(700L, "Time 6_2"), new TimeData(740L, "Time 6_3"))));

        long id = 0;
        ArrayList<StudentData> students = new ArrayList<>();
        students.add(getStudentData(0L, 1L, "Artyom Stukans", events));
        students.add(getStudentData(20L, 2L, "Julija Avdejeva", events));
        students.add(getStudentData(40L, 3L, "Taisija Poljakova", events));
        students.add(getStudentData(60L, 4L, "Petja Vasechkin", events));
        students.add(getStudentData(80L, 5L, "Petja Vasechkin", events));
        students.add(getStudentData(100L, 6L, "Petja Vasechkin", events));
        students.add(getStudentData(120L, 7L, "Petja Vasechkin", events));
        students.add(getStudentData(140L, 8L, "Petja Vasechkin", events));
        students.add(getStudentData(160L, 9L, "Petja Vasechkin", events));
        students.add(getStudentData(180L, 10L, "Petja Vasechkin", events));
        students.add(getStudentData(200L, 11L, "Petja Vasechkin", events));
        students.add(getStudentData(220L, 12L, "Petja Vasechkin", events));
        students.add(getStudentData(240L, 13L, "Petja Vasechkin", events));
        students.add(getStudentData(260L, 14L, "Petja Vasechkin", events));
        students.add(getStudentData(280L, 15L, "Petja Vasechkin", events));
        students.add(getStudentData(300L, 16L, "Petja Vasechkin", events));
        students.add(getStudentData(320L, 17L, "Petja Vasechkin", events));
        students.add(getStudentData(340L, 18L, "Petja Vasechkin", events));
        students.add(getStudentData(360L, 19L, "Petja Vasechkin", events));
        students.add(getStudentData(380L, 20L, "Petja Vasechkin", events));

        EventPlanningJournal data = new EventPlanningJournal("Heya", students, events);
        Map<String, Object> context = journalCreator.create(data);
        byte[] velocityRender = velocityEngine.render(cache.getTemplate(TemplateName.EventPlanningJournalVelocity), context, Type.HTML);
        FileUtils.writeByteArrayToFile(new File("planning_journal.jrxml"), velocityRender);

        byte[] jasperRender = jasperEngine.render(velocityRender, new HashMap<String, Object>(), Type.PDF);
        FileUtils.writeByteArrayToFile(new File("planning_journal.pdf"), jasperRender);
    }

    public StudentData getStudentData(Long yCoordinate, Long id, String name, List<EventData> events) {
        List<EventData> studentEvents = new ArrayList<>();
        for (EventData event : events) {
            studentEvents.add(event.clone());
        }

        return new StudentData(yCoordinate, id, name, studentEvents);

    }
}
