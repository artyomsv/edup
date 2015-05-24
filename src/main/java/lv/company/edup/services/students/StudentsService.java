package lv.company.edup.services.students;

import lv.company.edup.infrastructure.exceptions.BadRequestException;
import lv.company.edup.infrastructure.exceptions.NotFoundException;
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

    @Inject CurrentStudentVersionRepository currentStudentVersionRepository;
    @Inject StudentPropertyRepository propertyRepository;
    @Inject StudentVersionRepository versionRepository;
    @Inject ObjectMapper mapper;
    @Inject @JPA ODataSearchService searchService;
    @Inject UriUtils utils;

    public ODataResult<BaseStudentDto> search() {
        ODataCriteria critetia = new ODataCriteria(utils.getQueryParameters());
        ODataResult<CurrentStudentVersion> result = searchService.search(critetia, CurrentStudentVersion.class);
        List<CurrentStudentVersion> studentVersions = result.getValues();

        fetchBaseStudentProperties(studentVersions);

        List<BaseStudentDto> dtos = mapper.map(studentVersions, BaseStudentDto.class);
        return result.cloneFromValues(dtos);
    }

    public StudentDto findStudent(Long id) {
        Student student = currentStudentVersionRepository.find(id);
        if (student == null) {
            throw new NotFoundException();
        }

        List<StudentProperty> properties = propertyRepository.findByAttribute(student.getVersionId(), StudentProperty_.versionId);
        CollectionUtils.addAll(student.getProperties(), properties);

        student.setRootUrl(utils.getRootUrl() + "/secured/files/download");
        return mapper.map(student, StudentDto.class);
    }

    public Collection<BaseStudentDto> findVersions(Long id) {
        Collection<? extends Student> versions = versionRepository.findVersions(id);

        fetchBaseStudentProperties(versions);

        return mapper.map(versions, BaseStudentDto.class);
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
        return createdStudentVersion(dto, null);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public EntityPayload createdStudentVersion(StudentDto dto, Long id) {
        final StudentVersion version = mapper.map(dto, StudentVersion.class);
        version.setId(id == null ? currentStudentVersionRepository.getNextStudentId() : id);
        version.setCreated(new Date());
        versionRepository.persist(version);

        setVersionIdForProperties(version.getVersionId(), version.getProperties());

        propertyRepository.persist(version.getProperties());
        return new EntityPayload(version.getId(), version.getVersionId());
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public EntityPayload updateStudent(StudentDto dto, Long id) {
        CurrentStudentVersion currentVersion = currentStudentVersionRepository.find(id);
        if (currentVersion == null) {
            throw new NotFoundException();
        }

        return createdStudentVersion(dto, id);
    }


    private void fetchBaseStudentProperties(Collection<? extends Student> versions) {
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
                if (map.containsKey(version.getVersionId())) {
                    CollectionUtils.addAll(version.getProperties(), map.get(version.getVersionId()));
                }
            }
        }
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
}
