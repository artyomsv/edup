package lv.company.edup.services.students;

import lv.company.edup.infrastructure.exceptions.BadRequestException;
import lv.company.edup.infrastructure.exceptions.NotFoundException;
import lv.company.edup.infrastructure.mapping.ObjectTransformer;
import lv.company.edup.persistence.students.Student;
import lv.company.edup.persistence.students.current.StudentCurrent;
import lv.company.edup.persistence.students.current.StudentRepository;
import lv.company.edup.persistence.students.version.StudentVersion;
import lv.company.edup.persistence.students.version.StudentVersionRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class StudentsService {

    @Inject StudentRepository repository;
    @Inject StudentVersionRepository versionRepository;
    @Inject ObjectTransformer transformer;

    public Collection<StudentDto> findAll() {
        List<StudentCurrent> students = repository.findAll();
        return transformer.map(students, StudentDto.class);
    }

    public StudentDto findStudent(Long id) {
        Student student = repository.find(id);
        if (student == null) {
            throw new NotFoundException();
        }
        return transformer.map(student, StudentDto.class);
    }

    public Collection<StudentDto> findVersions(Long id) {
        Collection<? extends Student> versions = versionRepository.findVersions(id);
        return transformer.map(versions, StudentDto.class);
    }

    public StudentDto findVersion(Long id, Long versionId) {
        StudentVersion version = repository.find(versionId, StudentVersion.class);
        if (!version.getId().equals(id)) {
            throw new BadRequestException("Student ID doesnt match student version");
        }

        return transformer.map(version, StudentDto.class);
    }

    public Long createdStudent(StudentDto dto) {
        StudentVersion version = transformer.map(dto, StudentVersion.class);
        version.setId(repository.getNextStudentId());
        version.setCreated(new Date());
        versionRepository.persist(version);
        return version.getVersionId();
    }
}
