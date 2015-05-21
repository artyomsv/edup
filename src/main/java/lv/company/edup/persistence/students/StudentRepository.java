package lv.company.edup.persistence.students;

import lv.company.edup.persistence.EdupRepository;

import javax.ejb.Stateless;

@Stateless
public class StudentRepository extends EdupRepository<Student> {

    @Override
    public Class<Student> getEntityClass() {
        return Student.class;
    }
}
