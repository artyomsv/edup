package lv.company.edup.services.reports;

import lv.company.edup.infrastructure.templates.api.JasperEngine;
import lv.company.edup.infrastructure.templates.api.TemplateEngine;
import lv.company.edup.infrastructure.templates.api.TemplateName;
import lv.company.edup.infrastructure.templates.api.Type;
import lv.company.edup.infrastructure.templates.api.VelocityEngine;
import lv.company.edup.infrastructure.templates.impl.TemplateCache;
import lv.company.edup.infrastructure.templates.impl.templates.EventPlanningJournalContextCreator;
import lv.company.edup.infrastructure.templates.impl.templates.dto.EventData;
import lv.company.edup.infrastructure.templates.impl.templates.dto.EventPlanningJournal;
import lv.company.edup.infrastructure.templates.impl.templates.dto.StudentData;
import lv.company.edup.infrastructure.templates.impl.templates.dto.TimeData;
import lv.company.edup.infrastructure.utils.builder.IndexBuilder;
import lv.company.edup.persistence.subjects.view.AttendanceView;
import lv.company.edup.persistence.subjects.view.AttendanceView_;
import lv.company.edup.persistence.subjects.view.EventAttendanceRepository;
import lv.company.edup.persistence.subjects.view.SubjectEventRepository;
import lv.company.edup.persistence.subjects.view.SubjectEvents;
import lv.company.edup.services.subjects.dto.SubjectDto;
import lv.company.odata.api.ODataSearchService;
import lv.company.odata.impl.JPA;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.time.DateFormatUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ReportsService {

    @Inject SubjectEventRepository eventRepository;
    @Inject EventAttendanceRepository attendanceRepository;
    @Inject @JPA ODataSearchService searchService;
    @Inject @VelocityEngine TemplateEngine velocityEngine;
    @Inject @JasperEngine TemplateEngine jasperEngine;
    @Inject TemplateCache cache;


    public byte[] renderVisitingJournalPlan(SubjectDto subjectDto, Date from, Date to, boolean withParticipation) {

        Collection<SubjectEvents> events = eventRepository.findEvents(subjectDto.getSubjectId(), from, to);

        Map<String, Collection<SubjectEvents>> eventsMap = buildEventsMap(events);

        long initCoordinate = 140L;
        List<EventData> templateEvents = new ArrayList<>();
        for (Map.Entry<String, Collection<SubjectEvents>> entry : eventsMap.entrySet()) {
            List<TimeData> templateTimeEvents = buildTimeEventsForHeader(initCoordinate, (List<SubjectEvents>) entry.getValue());
            templateEvents.add(new EventData(initCoordinate, entry.getKey(), templateTimeEvents));
            initCoordinate += CollectionUtils.size(templateTimeEvents) * 40;
        }

        Map<String, Collection<AttendanceView>> attendanceMap = IndexBuilder.<String, AttendanceView>get()
                .key(new Transformer<AttendanceView, String>() {
                    @Override
                    public String transform(AttendanceView input) {
                        return input.getName() + " " + input.getLastName();
                    }
                })
                .mapToCollection(attendanceRepository.findByAttribute(getEventIds(events), AttendanceView_.eventId));

        ArrayList<StudentData> students = new ArrayList<>();
        long yCoordinate = 0L;
        long id = 0L;

        for (Map.Entry<String, Collection<AttendanceView>> entry : attendanceMap.entrySet()) {
            students.add(getStudentData(yCoordinate, ++id, entry.getKey(), templateEvents, withParticipation ? getStudentAttendanceToEvents(entry) : Collections.<Long, AttendanceView>emptyMap()));
        }

        EventPlanningJournal data = new EventPlanningJournal(subjectDto.getSubjectName(), students, templateEvents);
        Map<String, Object> context = new EventPlanningJournalContextCreator().create(data);
        byte[] velocityRender = velocityEngine.render(cache.getTemplate(TemplateName.EventPlanningJournalVelocity), context, Type.HTML);

        return jasperEngine.render(velocityRender, new HashMap<String, Object>(), Type.PDF);
    }

    private Map<Long, AttendanceView> getStudentAttendanceToEvents(Map.Entry<String, Collection<AttendanceView>> entry) {
        return IndexBuilder.<Long, AttendanceView>get()
                .key(new Transformer<AttendanceView, Long>() {
                    @Override
                    public Long transform(AttendanceView input) {
                        return input.getEventId();
                    }
                })
                .map(entry.getValue());
    }

    private Collection<Long> getEventIds(Collection<SubjectEvents> events) {
        return CollectionUtils.collect(events, new Transformer<SubjectEvents, Long>() {
            @Override
            public Long transform(SubjectEvents input) {
                return input.getEventId();
            }
        });
    }

    private Map<String, Collection<SubjectEvents>> buildEventsMap(Collection<SubjectEvents> events) {
        return IndexBuilder.<String, SubjectEvents>get()
                .key(new Transformer<SubjectEvents, String>() {
                    @Override
                    public String transform(SubjectEvents input) {
                        return DateFormatUtils.format(input.getEventDate(), "dd/MM/yyyy");
                    }
                })
                .indexForCollections(new Factory<Map<String, Collection<SubjectEvents>>>() {
                    @Override
                    public Map<String, Collection<SubjectEvents>> create() {
                        return new LinkedHashMap<String, Collection<SubjectEvents>>();
                    }
                })
                .factory(new Factory<Collection<SubjectEvents>>() {
                    @Override
                    public Collection<SubjectEvents> create() {
                        return new ArrayList<SubjectEvents>();
                    }
                })
                .mapToCollection(events);
    }

    private HashSet<String> getStudentsNamesList(List<AttendanceView> attendances) {
        return new HashSet<>(CollectionUtils.collect(attendances, new Transformer<AttendanceView, String>() {
            @Override
            public String transform(AttendanceView input) {
                return input.getName() + " " + input.getLastName();
            }
        }));
    }

    public StudentData getStudentData(Long yCoordinate, Long id, String name, List<EventData> events, Map<Long, AttendanceView> studentAttendance) {
        List<EventData> studentEvents = new ArrayList<>();
        for (EventData event : events) {
            studentEvents.add(event.copyObject(studentAttendance));
        }

        return new StudentData(yCoordinate, id, name, studentEvents);

    }

    private List<TimeData> buildTimeEventsForHeader(long initCoordinate, List<SubjectEvents> events) {
        Collections.sort(events, new Comparator<SubjectEvents>() {
            @Override
            public int compare(SubjectEvents o1, SubjectEvents o2) {
                return o1.getFrom().compareTo(o2.getFrom());
            }
        });

        List<TimeData> timeColumnData = new ArrayList<>();
        for (SubjectEvents event : events) {
            timeColumnData.add(new TimeData(initCoordinate, formatTime(event.getFrom(), event.getTo()), event.getEventId()));
            initCoordinate += 40;
        }
        return timeColumnData;

    }

    private String formatTime(Date from, Date to) {
        return DateFormatUtils.format(from, "HH:mm") + " - " + DateFormatUtils.format(to, "HH:mm");
    }

}
