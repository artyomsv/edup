package lv.company.edup.persistence.students.properties;

import lv.company.edup.persistence.EdupRepository;

import javax.ejb.Stateless;

@Stateless
public class StudentPropertyRepository extends EdupRepository<StudentProperty> {

    @Override
    public Class<StudentProperty> getEntityClass() {
        return StudentProperty.class;
    }

}
