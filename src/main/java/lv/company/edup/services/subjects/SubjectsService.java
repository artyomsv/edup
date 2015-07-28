package lv.company.edup.services.subjects;

import lv.company.edup.infrastructure.exceptions.BadRequestException;
import lv.company.edup.infrastructure.exceptions.NotFoundException;
import lv.company.edup.infrastructure.lucene.api.indexer.SubjectWriter;
import lv.company.edup.infrastructure.lucene.api.searcher.SubjectSearcher;
import lv.company.edup.infrastructure.lucene.impl.indexer.SubjectsIndexWriter;
import lv.company.edup.infrastructure.lucene.impl.searcher.SubjectIndexSearcher;
import lv.company.edup.infrastructure.mapping.ObjectMapper;
import lv.company.edup.infrastructure.utils.FakeUtils;
import lv.company.edup.persistence.students.current.CurrentStudentVersion;
import lv.company.edup.persistence.students.current.CurrentStudentVersionRepository;
import lv.company.edup.persistence.subjects.AttendanceRepository;
import lv.company.edup.persistence.subjects.EventRepository;
import lv.company.edup.persistence.subjects.EventStatus;
import lv.company.edup.persistence.subjects.SubjectRepository;
import lv.company.edup.persistence.subjects.domain.Attendance;
import lv.company.edup.persistence.subjects.domain.Attendance_;
import lv.company.edup.persistence.subjects.domain.Event;
import lv.company.edup.persistence.subjects.domain.Subject;
import lv.company.edup.persistence.subjects.view.AttendanceView;
import lv.company.edup.persistence.subjects.view.SubjectEventRepository;
import lv.company.edup.persistence.subjects.view.SubjectEvents;
import lv.company.edup.services.subjects.dto.AttendanceDto;
import lv.company.edup.services.subjects.dto.EventDto;
import lv.company.edup.services.subjects.dto.SubjectDto;
import lv.company.odata.api.ODataCriteria;
import lv.company.odata.api.ODataResult;
import lv.company.odata.api.ODataSearchService;
import lv.company.odata.api.SearchOperator;
import lv.company.odata.impl.JPA;
import org.apache.commons.collections4.CollectionUtils;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.metamodel.SingularAttribute;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class SubjectsService {

    @Inject @SubjectSearcher SubjectIndexSearcher searcher;
    @Inject @SubjectWriter SubjectsIndexWriter writer;

    @Inject ObjectMapper mapper;
    @Inject SubjectRepository subjectRepository;
    @Inject EventRepository eventRepository;
    @Inject CurrentStudentVersionRepository studentRepository;
    @Inject AttendanceRepository attendanceRepository;
    @Inject SubjectEventRepository subjetEventRepository;

    @Inject @JPA ODataSearchService searchService;

    public SubjectDto find(Long id) {
        return mapper.map(subjectRepository.find(id), SubjectDto.class);
    }

    public ODataResult<SubjectDto> search(ODataCriteria criteria) {
        ODataResult<SubjectDto> search;
        try {
            search = searcher.search(criteria);
            List<SubjectDto> dtos = mapper.map(search.getValues(), SubjectDto.class);
            search = search.cloneFromValues(dtos);
        } catch (Exception e) {
            search = new ODataResult<SubjectDto>();
            search.setCount(0L);
        }

        return search;
    }

    public Long createSubject(SubjectDto dto) {

        Subject subject = new Subject();
        subject.setSubjectName(dto.getSubjectName());
        subject.setCreated(new Date());
        subjectRepository.persist(subject);

        dto.setCreated(subject.getCreated());
        dto.setSubjectId(subject.getSubjectId());
        index(dto);

        return subject.getSubjectId();
    }

    public void index(SubjectDto dto) {
        writer.add(dto);
    }

    public ODataResult<EventDto> searchSubjectEvents(ODataCriteria criteria) {
        ODataResult<SubjectEvents> result = searchService.search(criteria, SubjectEvents.class);
        List<EventDto> subjects = mapper.map(result.getValues(), EventDto.class);
        return result.cloneFromValues(subjects);
    }

    public Long createSubjectEvent(EventDto dto) {
        SubjectDto subjectDto = dto.getSubject();
        Long subjectId;
        if (subjectDto.getSubjectId() != null) {
            Subject subject = subjectRepository.find(subjectDto.getSubjectId());
            if (subject == null) {
                throw new BadRequestException("Could not map event to subject");
            }
            subjectId = subject.getSubjectId();
        } else {
            ODataResult<SubjectDto> result = searchSubject(subjectDto);
            if (result.getCount() != 1) {
                subjectId = createSubject(subjectDto);
            } else {
                subjectId = result.getValues().iterator().next().getSubjectId();
            }
        }

        Event event = new Event();
        event.setSubjectId(subjectId);
        event.setCreated(new Date());
        event.setUpdated(new Date());
        event.setEventDate(dto.getEventDate());
        event.setFrom(dto.getFrom());
        event.setTo(dto.getTo());
        event.setPrice(dto.getPrice());
        event.setStatus(EventStatus.PLANNED);

        eventRepository.persist(event);

        return event.getEventId();
    }

    public ODataResult<AttendanceDto> searchAttendance(ODataCriteria criteria) {
        ODataResult<AttendanceView> result = searchService.search(criteria, AttendanceView.class);
        List<AttendanceDto> values = mapper.map(result.getValues(), AttendanceDto.class);
        return result.cloneFromValues(values);
    }

    public Long createAttendance(AttendanceDto dto, Long eventId) {
        Event event = eventRepository.find(eventId);
        if (event == null) {
            throw new BadRequestException("Could not map attendance to event");
        }

        CurrentStudentVersion studentVersion = studentRepository.find(dto.getStudentId());
        if (studentVersion == null) {
            throw new BadRequestException("Could not map student to attendance");
        }

        HashMap<Collection<?>, SingularAttribute<Attendance, ?>> pairs = new HashMap<>();
        pairs.put(Collections.singleton(eventId), Attendance_.eventId);
        pairs.put(Collections.singleton(dto.getStudentId()), Attendance_.studentId);
        List<Attendance> attendances = attendanceRepository.findByMultipleAttributes(pairs);
        if (CollectionUtils.isNotEmpty(attendances)) {
            throw new BadRequestException("Attendance for student " + getStudentName(studentVersion) + " is already saved");
        }

        Attendance attendance = new Attendance();
        attendance.setEventId(eventId);
        attendance.setStudentId(dto.getStudentId());
        attendance.setCreated(new Date());
        attendance.setUpdated(new Date());
        attendance.setBalanceAdjusted(false);
        attendance.setParticipated(true);
        attendance.setNotified(false);

        attendanceRepository.persist(attendance);

        return attendance.getAttendanceId();
    }

    private String getStudentName(CurrentStudentVersion studentVersion) {
        return studentVersion.getName() + " " + studentVersion.getLastName();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void updateAttendance(AttendanceDto dto, Long eventId, Long attendanceId) {
        Attendance attendance = attendanceRepository.find(attendanceId);
        if (attendance == null) {
            throw new NotFoundException("Could not update missing attendance");
        }

        if (!attendance.getEventId().equals(eventId)) {
            throw new NotFoundException("Current attendance is not mapped to event");
        }

        if (dto.getNotified() != null) {
            attendance.setNotified(dto.getNotified());
        }

        if (dto.getParticipated() != null) {
            attendance.setParticipated(dto.getParticipated());
        }

        if (dto.getBalanceAdjusted() != null) {
            attendance.setBalanceAdjusted(dto.getBalanceAdjusted());
        }

        attendance.setUpdated(new Date());

    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteAttendance(Long eventId, Long attendanceId) {
        Attendance attendance = attendanceRepository.find(attendanceId);
        if (attendance == null) {
            throw new NotFoundException("Could not update missing attendance");
        }

        if (!attendance.getEventId().equals(eventId)) {
            throw new NotFoundException("Current attendance is not mapped to event");
        }

        attendanceRepository.delete(attendance);
    }

    public void rebuildIndex() {
        writer.add(mapper.map(subjectRepository.findAll(), SubjectDto.class));
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Deprecated
    public void generateSubjectEvents(long subjectId) {
        for (int i = 0; i < 100; i++) {
            createSubjectEvent(FakeUtils.buildEvent(subjectId));
        }
    }

    public EventDto getEventDetails(Long eventId) {
        SubjectEvents details = subjetEventRepository.find(eventId);
        EventDto detailsDto = null;
        if (details != null) {
            detailsDto = mapper.map(details, EventDto.class);
            ODataCriteria criteria = new ODataCriteria();
            criteria.getAllValues().setCount(true).appendCustomFilter("EventId", SearchOperator.equal(), String.valueOf(eventId));
            ODataResult<AttendanceView> search = searchService.search(criteria, AttendanceView.class);
            detailsDto.setStudents(search.getCount());
        }
        return detailsDto;
    }

    public ODataResult<SubjectDto> searchSubject(SubjectDto dto) {
        ODataCriteria criteria = new ODataCriteria()
                .getAllValues()
                .setCount(true)
                .setFilter("Name eq '" + dto.getSubjectName() + "'");

        return search(criteria);
    }

    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public void updateEvent(EventDto dto) {
        Event event = eventRepository.find(dto.getEventId());
        if (event == null) {
            throw new NotFoundException("Missing event with [" + dto.getEventId() + "] ID");
        }

        if (event.getStatus() == EventStatus.FINALIZED) {
            throw new BadRequestException("Event is finalized and cannot be updated");
        }

        mapper.map(dto, event);
    }
}
