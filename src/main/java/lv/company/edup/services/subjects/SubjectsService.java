package lv.company.edup.services.subjects;

import lv.company.edup.infrastructure.exceptions.BadRequestException;
import lv.company.edup.infrastructure.lucene.api.indexer.SubjectWriter;
import lv.company.edup.infrastructure.lucene.api.searcher.SubjectSearcher;
import lv.company.edup.infrastructure.lucene.impl.indexer.SubjectsIndexWriter;
import lv.company.edup.infrastructure.lucene.impl.searcher.SubjectIndexSearcher;
import lv.company.edup.infrastructure.mapping.ObjectMapper;
import lv.company.edup.persistence.subjects.EventRepository;
import lv.company.edup.persistence.subjects.EventStatus;
import lv.company.edup.persistence.subjects.SubjectRepository;
import lv.company.edup.persistence.subjects.domain.Event;
import lv.company.edup.persistence.subjects.domain.Subject;
import lv.company.edup.persistence.subjects.view.EventView;
import lv.company.edup.services.subjects.dto.AttendanceDto;
import lv.company.edup.services.subjects.dto.EventDto;
import lv.company.edup.services.subjects.dto.SubjectDto;
import lv.company.odata.api.ODataCriteria;
import lv.company.odata.api.ODataResult;
import lv.company.odata.api.ODataSearchService;
import lv.company.odata.impl.JPA;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class SubjectsService {

    @Inject @SubjectSearcher SubjectIndexSearcher searcher;
    @Inject @SubjectWriter SubjectsIndexWriter writer;

    @Inject ObjectMapper mapper;
    @Inject SubjectRepository subjectRepository;
    @Inject EventRepository eventRepository;

    @Inject @JPA ODataSearchService searchService;

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
        ODataCriteria criteria = new ODataCriteria()
                .getAllValues()
                .setHead(true)
                .setCount(true)
                .setFilter("Name eq " + dto.getSubjectName());

        ODataResult<SubjectDto> search = search(criteria);
        if (search.getCount() != 0) {
            throw new BadRequestException("Subject name already used");
        }

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
        ODataResult<EventView> result = searchService.search(criteria, EventView.class);
        List<EventDto> subjects = mapper.map(result.getValues(), EventDto.class);
        return result.cloneFromValues(subjects);
    }

    public Long createSubjectEvent(EventDto dto, Long subjectId) {
        Subject subject = subjectRepository.find(subjectId);
        if (subject == null) {
            throw new BadRequestException("Could not map event to subject");
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

    public ODataResult<AttendanceDto> getEventAttendance(Long subjectId, Long eventId) {
//        ODataCriteria

        return null;
    }
}
