package lv.company.edup.services.students;

import lv.company.edup.infrastructure.exceptions.BadRequestException;
import lv.company.edup.infrastructure.exceptions.NotFoundException;
import lv.company.edup.infrastructure.mapping.ObjectMapper;
import lv.company.edup.infrastructure.response.UriUtils;
import lv.company.edup.persistence.EntityPayload;
import lv.company.edup.persistence.students.Student;
import lv.company.edup.persistence.students.current.CurrentStudentVersion;
import lv.company.edup.persistence.students.current.CurrentStudentVersionRepository;
import lv.company.edup.persistence.students.version.StudentVersion;
import lv.company.edup.persistence.students.version.StudentVersionRepository;
import lv.company.odata.api.ODataCriteria;
import lv.company.odata.api.ODataResult;
import lv.company.odata.api.ODataSearchService;
import lv.company.odata.impl.JPA;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class StudentsService {

    @Inject CurrentStudentVersionRepository repository;
    @Inject StudentVersionRepository versionRepository;
    @Inject ObjectMapper mapper;
    @Inject @JPA ODataSearchService searchService;
    @Inject UriUtils utils;

    public ODataResult<StudentDto> search() {
        ODataCriteria critetia = new ODataCriteria(utils.getQueryParameters());
        ODataResult<CurrentStudentVersion> result = searchService.search(critetia, CurrentStudentVersion.class);
        List<CurrentStudentVersion> studentVersions = result.getValues();

        List<StudentDto> dtos = mapper.map(studentVersions, StudentDto.class);
        return result.cloneFromValues(dtos);
    }

    public StudentDto findStudent(Long id) {
        Student student = repository.find(id);
        if (student == null) {
            throw new NotFoundException();
        }
        return mapper.map(student, StudentDto.class);
    }

    public Collection<StudentDto> findVersions(Long id) {
        Collection<? extends Student> versions = versionRepository.findVersions(id);
        return mapper.map(versions, StudentDto.class);
    }

    public StudentDto findVersion(Long id, Long versionId) {
        StudentVersion version = repository.find(versionId, StudentVersion.class);
        if (!version.getId().equals(id)) {
            throw new BadRequestException("Student ID doesnt match student version");
        }

        return mapper.map(version, StudentDto.class);
    }

    public EntityPayload createStudentVersion(StudentDto dto) {
        return createdStudentVersion(dto, null);
    }


    public EntityPayload createdStudentVersion(StudentDto dto, Long id) {
        StudentVersion version = mapper.map(dto, StudentVersion.class);
        version.setId(id == null ? repository.getNextStudentId() : id);
        version.setCreated(new Date());
        versionRepository.persist(version);
        return new EntityPayload(version.getId(), version.getVersionId());
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public EntityPayload updateStudent(StudentDto dto, Long id) {
        CurrentStudentVersion currentVersion = repository.find(id);
        if (currentVersion == null) {
            throw new NotFoundException();
        }

        return createdStudentVersion(dto, id);
    }
}
