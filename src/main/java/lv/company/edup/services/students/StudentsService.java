package lv.company.edup.services.students;

import lv.company.edup.infrastructure.exceptions.BadRequestException;
import lv.company.edup.infrastructure.exceptions.NotFoundException;
import lv.company.edup.infrastructure.lucene.api.indexer.StudentWriter;
import lv.company.edup.infrastructure.lucene.api.searcher.StudentReader;
import lv.company.edup.infrastructure.lucene.impl.indexer.StudentsIndexWriter;
import lv.company.edup.infrastructure.lucene.impl.searcher.StudentsSearcher;
import lv.company.edup.infrastructure.mapping.ObjectMapper;
import lv.company.edup.infrastructure.response.UriUtils;
import lv.company.edup.infrastructure.utils.builder.IndexBuilder;
import lv.company.edup.persistence.EntityPayload;
import lv.company.edup.persistence.students.Student;
import lv.company.edup.persistence.students.current.CurrentStudentVersion;
import lv.company.edup.persistence.students.current.CurrentStudentVersionRepository;
import lv.company.edup.persistence.students.properties.PropertyName;
import lv.company.edup.persistence.students.properties.StudentProperty;
import lv.company.edup.persistence.students.properties.StudentPropertyRepository;
import lv.company.edup.persistence.students.properties.StudentProperty_;
import lv.company.edup.persistence.students.version.StudentVersion;
import lv.company.edup.persistence.students.version.StudentVersionRepository;
import lv.company.edup.services.students.dto.LeanStudentDto;
import lv.company.edup.services.students.dto.StudentDto;
import lv.company.odata.api.ODataCriteria;
import lv.company.odata.api.ODataResult;
import lv.company.odata.api.ODataSearchService;
import lv.company.odata.impl.JPA;
import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.StringUtils;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.metamodel.SingularAttribute;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class StudentsService {

    private static final String SECURED_FILES_DOWNLOAD = "/private/files/download";

    @Inject @StudentReader StudentsSearcher searcher;
    @Inject @StudentWriter StudentsIndexWriter indexer;

    @Inject CurrentStudentVersionRepository currentStudentVersionRepository;
    @Inject StudentPropertyRepository propertyRepository;
    @Inject StudentVersionRepository versionRepository;
    @Inject ObjectMapper mapper;
    @Inject @JPA ODataSearchService searchService;
    @Inject UriUtils utils;

    public ODataResult<LeanStudentDto> search() {
        ODataCriteria criteria = new ODataCriteria(utils.getQueryParameters());

        if (!criteria.isNotEmpty()) {
            criteria.setSearch("*");
        }

        ODataResult<StudentDto> result;
        try {
            result = searcher.search(criteria);
        } catch (Exception e) {
            result = new ODataResult<>();
            result.setCount(0L);
        }

        List<LeanStudentDto> dtos = mapper.map(result.getValues(), LeanStudentDto.class);
        return result.cloneFromValues(dtos);
    }

    public StudentDto findLeanStudent(Long id) {
        Student student = fetchLeanStudent(id);
        return mapper.map(student, StudentDto.class);
    }

    public StudentDto findStudent(Long id) {
        Student student = fetchLeanStudent(id);

        List<StudentProperty> properties = propertyRepository.findByAttribute(student.getVersionId(), StudentProperty_.versionId);
        CollectionUtils.addAll(student.getProperties(), properties);

        return mapper.map(student, StudentDto.class);
    }

    public ODataResult<LeanStudentDto> findVersions(Long id) {
        Collection<? extends Student> versions = versionRepository.findVersions(id);

        fetchBaseStudentProperties(versions);

        List<LeanStudentDto> list = mapper.map(versions, LeanStudentDto.class);
        ODataResult<LeanStudentDto> result = new ODataResult<LeanStudentDto>();
        result.setValues(list);
        return result;
    }

    public StudentDto findVersion(Long id, Long versionId) {
        StudentVersion version = propertyRepository.find(versionId, StudentVersion.class);
        if (!version.getId().equals(id)) {
            throw new BadRequestException("Student ID doesnt match student version");
        }

        List<StudentProperty> properties = propertyRepository.findByAttribute(version.getVersionId(), StudentProperty_.versionId);
        CollectionUtils.addAll(version.getProperties(), properties);

        return mapper.map(version, StudentDto.class);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public EntityPayload createStudentVersion(StudentDto dto) {
        return createStudentVersion(dto, null);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public EntityPayload createStudentVersion(StudentDto dto, Long id) {
        final StudentVersion version = mapper.map(dto, StudentVersion.class);
        version.setVersionId(null);
        version.setId(id == null ? currentStudentVersionRepository.getNextStudentId() : id);
        version.setCreated(new Date());
        versionRepository.persist(version);

        setVersionIdForProperties(version.getVersionId(), version.getProperties());

        propertyRepository.persist(version.getProperties());

        dto.setId(version.getId());
        dto.setVersionId(version.getVersionId());
        return new EntityPayload(version.getId(), version.getVersionId());
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public EntityPayload updateStudent(StudentDto dto, Long id, String etag) {
        CurrentStudentVersion currentVersion = currentStudentVersionRepository.find(id);
        if (currentVersion == null) {
            throw new NotFoundException();
        }

        if (!StringUtils.equals(etag, String.valueOf(currentVersion.getVersionId()))) {
            throw new BadRequestException("Etag is outdated");
        }

        return createStudentVersion(dto, id);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void fetchBaseStudentProperties(Collection<? extends Student> versions) {
        if (CollectionUtils.isEmpty(versions)) {
            return;
        }
        Collection<Long> versionIds = new HashSet<Long>();
        for (Student version : versions) {
            versionIds.add(version.getVersionId());
        }

        HashMap<Collection<?>, SingularAttribute<StudentProperty, ?>> pairs = new HashMap<Collection<?>, SingularAttribute<StudentProperty, ?>>();
        pairs.put(versionIds, StudentProperty_.versionId);
        pairs.put(Arrays.asList(PropertyName.BIRTH_DATE, PropertyName.MOBILE_PHONE, PropertyName.PERSONAL_NUMBER), StudentProperty_.name);
        List<StudentProperty> properties = propertyRepository.findByMultipleAttributes(pairs);
        if (CollectionUtils.isNotEmpty(properties)) {

            Map<Long, Collection<StudentProperty>> map = IndexBuilder.<Long, StudentProperty>get()
                    .key(new Transformer<StudentProperty, Long>() {
                        @Override
                        public Long transform(StudentProperty input) {
                            return input.getVersionId();
                        }
                    })
                    .mapToCollection(properties);

            for (Student version : versions) {
                if (version == null) {
                    continue;
                }
                version.setRootUrl(utils.getRootUrl() + SECURED_FILES_DOWNLOAD);
                if (map.containsKey(version.getVersionId())) {
                    CollectionUtils.addAll(version.getProperties(), map.get(version.getVersionId()));
                }
            }
        }
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void updateIndex(Long id) {
        StudentDto student = findStudent(id);
        indexer.add(student);
    }

    private void setVersionIdForProperties(final Long versionId, List<StudentProperty> properties) {
        if (CollectionUtils.isNotEmpty(properties)) {
            CollectionUtils.forAllDo(properties, new Closure<StudentProperty>() {
                @Override
                public void execute(StudentProperty input) {
                    input.setVersionId(versionId);
                }
            });
        }
    }

    public Student fetchLeanStudent(Long id) {
        Student student = currentStudentVersionRepository.find(id);
        if (student == null) {
            throw new NotFoundException("Student with ID:" + id + " not found!");
        }

        student.setRootUrl(utils.getRootUrl() + SECURED_FILES_DOWNLOAD);
        return student;
    }
}
