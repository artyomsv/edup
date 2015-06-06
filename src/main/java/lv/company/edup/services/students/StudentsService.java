package lv.company.edup.services.students;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import lv.company.edup.infrastructure.exceptions.BadRequestException;
import lv.company.edup.infrastructure.exceptions.NotFoundException;
import lv.company.edup.infrastructure.lucene.api.config.StudentReader;
import lv.company.edup.infrastructure.lucene.api.config.StudentWriter;
import lv.company.edup.infrastructure.lucene.impl.indexer.StudentsIndexer;
import lv.company.edup.infrastructure.lucene.impl.searcher.StudentsSearcher;
import lv.company.edup.infrastructure.mapping.ObjectMapper;
import lv.company.edup.infrastructure.response.UriUtils;
import lv.company.edup.infrastructure.utils.AppCollectionUtils;
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
import lv.company.edup.services.students.dto.BaseStudentDto;
import lv.company.edup.services.students.dto.StudentDto;
import lv.company.odata.api.ODataCriteria;
import lv.company.odata.api.ODataResult;
import lv.company.odata.api.ODataSearchService;
import lv.company.odata.impl.JPA;
import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.commons.lang3.time.DateUtils;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.metamodel.SingularAttribute;
import java.text.ParseException;
import java.util.ArrayList;
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
    @Inject @StudentWriter StudentsIndexer indexer;

    @Inject CurrentStudentVersionRepository currentStudentVersionRepository;
    @Inject StudentPropertyRepository propertyRepository;
    @Inject StudentVersionRepository versionRepository;
    @Inject ObjectMapper mapper;
    @Inject @JPA ODataSearchService searchService;
    @Inject UriUtils utils;

    public ODataResult<BaseStudentDto> search() {
        ODataCriteria critetia = new ODataCriteria(utils.getQueryParameters());

        if (!critetia.isNotEmpty()) {
            critetia.setSearch("*");
        }
        ODataResult<StudentDto> result = searcher.search(critetia);

        List<BaseStudentDto> dtos = mapper.map(result.getValues(), BaseStudentDto.class);
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

    public ODataResult<BaseStudentDto> findVersions(Long id) {
        Collection<? extends Student> versions = versionRepository.findVersions(id);

        fetchBaseStudentProperties(versions);

        List<BaseStudentDto> list = mapper.map(versions, BaseStudentDto.class);
        ODataResult<BaseStudentDto> result = new ODataResult<BaseStudentDto>();
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
        version.setId(id == null ? currentStudentVersionRepository.getNextStudentId() : id);
        Date created = new Date();
        version.setCreated(created);
        versionRepository.persist(version);

        setVersionIdForProperties(version.getVersionId(), version.getProperties());

        propertyRepository.persist(version.getProperties());

        dto.setId(version.getId());
        dto.setVersionId(version.getVersionId());
        return new EntityPayload(version.getId(), version.getVersionId());
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public EntityPayload updateStudent(StudentDto dto, Long id) {
        CurrentStudentVersion currentVersion = currentStudentVersionRepository.find(id);
        if (currentVersion == null) {
            throw new NotFoundException();
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
            Map<Long, Collection<StudentProperty>> map = AppCollectionUtils.mapToCollection(properties, new AppCollectionUtils.KeyTransformer<StudentProperty, Long>() {
                @Override
                public Long transform(StudentProperty studentProperty) {
                    return studentProperty.getVersionId();
                }
            });
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


    @Asynchronous
    public void updateIndex(StudentDto dto) {
        indexer.add(dto);
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

    public Boolean fillFakeData(int count) {
        Faker faker = new Faker();

        List<StudentDto> dtos = new ArrayList<StudentDto>();
        for (int i = 0; i < count; i++) {
            StudentDto dto = new StudentDto();
            Name name = faker.name();
            dto.setName(name.firstName());
            dto.setLastName(name.lastName());
            dto.setParentsInfo(faker.lorem().paragraph());
            dto.setCharacteristics(faker.lorem().paragraph());
            dto.setMail(faker.internet().emailAddress());

            int year = random(1995, 2010);
            int month = random(1, 12);
            int date = random(1, 28);

            String birthDate = String.format("%04d%02d%02d", year, month, date);

            dto.setPersonId(String.format("%02d%02d%04d-%5d", date, month, year, random(10000, 50000)));
            dto.setMobile(String.format("%s", random(28000000, 29000000)));
            try {
                dto.setBirthDate(DateUtils.parseDate(birthDate, "yyyyMMdd"));
            } catch (ParseException e) {

            }
            createStudentVersion(dto);
            dtos.add(dto);
        }
        indexer.add(dtos);
        return true;
    }

    private Integer random(int from, int to) {
        return RandomUtils.nextInt(from, to);
    }

    private String getRandomDate() {
        StrBuilder strBuilder = new StrBuilder();
        strBuilder.append(RandomUtils.nextInt(1995, 2010))
                .append(String.format("%2d", RandomUtils.nextInt(0, 11)))
                .append(String.format("%2d", RandomUtils.nextInt(0, 28)));
        return strBuilder.toString();
    }

    public void rebuild() {
        indexer.rebuild();
    }

    public Student fetchLeanStudent(Long id) {
        Student student = currentStudentVersionRepository.find(id);
        if (student == null) {
            throw new NotFoundException();
        }

        student.setRootUrl(utils.getRootUrl() + SECURED_FILES_DOWNLOAD);
        return student;
    }
}
