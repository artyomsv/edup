package lv.company.edup.reports;

import com.github.javafaker.Faker;
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
import lv.company.edup.persistence.subjects.view.AttendanceView;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
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
        events.add(new EventData(140L, "20/07/201", Arrays.asList(new TimeData(140L, "10:00 - 11:00", 1L), new TimeData(180L, "12:00 - 14:00", 2L))));
        events.add(new EventData(220L, "21/07/2015", Arrays.asList(new TimeData(220L, "09:00 - 10:30", 3L), new TimeData(260L, "12:15 - 14:30", 4L), new TimeData(300L, "15:00 - 16:00", 5L))));
        events.add(new EventData(340L, "22/07/2015", Arrays.asList(new TimeData(340L, "10:00 - 12:00", 6L), new TimeData(380L, "13:00 - 14:00", 7L), new TimeData(420L, "17:00 - 18:00", 8L))));
        events.add(new EventData(460L, "23/07/2015", Arrays.asList(new TimeData(460L, "12:00 - 14:00", 9L))));
        events.add(new EventData(500L, "24/07/2015", Arrays.asList(new TimeData(500L, "10:00 - 12:00", 11L), new TimeData(540L, "13:00 - 14:30", 12L), new TimeData(580L, "18:00 - 20:00", 13L))));
//        events.add(new EventData(660L, "Date6", Arrays.asList(new TimeData(660L, "Time 6_1", 14L), new TimeData(700L, "Time 6_2", 15L), new TimeData(740L, "Time 6_3", 16L))));
//        events.add(new EventData(780L, "Date7", Arrays.asList(new TimeData(780L, "Time 7_1", 17L), new TimeData(820L, "Time 7_2", 18L), new TimeData(860L, "Time 7_3", 19L))));
//        events.add(new EventData(900L, "Date8", Arrays.asList(new TimeData(900L, "Time 8_1", 20L), new TimeData(940L, "Time 8_2", 21L), new TimeData(980L, "Time 8_3", 22L))));

        ArrayList<StudentData> students = new ArrayList<>();
        Faker faker = new Faker();
        for (int i = 0; i < 10; i++) {
            HashMap<Long, AttendanceView> participation = new HashMap<>();
            AttendanceView value = new AttendanceView();
            value.setParticipated(true);
            participation.put(1L, value);
            students.add(getStudentData(0L, (long) i + 1, faker.name().fullName(), events, participation));
        }

        EventPlanningJournal data = new EventPlanningJournal("Heya", students, events);
        Map<String, Object> context = journalCreator.create(data);
        byte[] velocityRender = velocityEngine.render(cache.getTemplate(TemplateName.EventPlanningJournalVelocity), context, Type.HTML);
        FileUtils.writeByteArrayToFile(new File("planning_journal.jrxml"), velocityRender);

        byte[] jasperRender = jasperEngine.render(velocityRender, new HashMap<String, Object>(), Type.PDF);
        FileUtils.writeByteArrayToFile(new File("planning_journal.pdf"), jasperRender);
    }

    @Test
    @Ignore
    public void testName() throws Exception {

        byte[] bytes = FileUtils.readFileToByteArray(new File("planning_journal.xml"));
        if (bytes != null) {
            FileUtils.writeByteArrayToFile(new File("planning_journal.pdf"), jasperEngine.render(bytes, new HashMap<String, Object>(), Type.PDF));
        }

    }

    public StudentData getStudentData(Long yCoordinate, Long id, String name, List<EventData> events, Map<Long, AttendanceView> participation) {
        List<EventData> studentEvents = new ArrayList<>();
        for (EventData event : events) {
            EventData clone = event.copyObject(participation);
            studentEvents.add(clone);
        }

        return new StudentData(yCoordinate, id, name, studentEvents);

    }
}
