package lv.company.edup.services.students.mappers;

import lv.company.edup.infrastructure.mapping.CustomMapper;
import lv.company.edup.persistence.students.Student;
import lv.company.edup.persistence.students.properties.PropertyName;
import lv.company.edup.services.students.AgeService;
import lv.company.edup.services.students.dto.BaseStudentDto;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;

public class BaseStudentMapper implements CustomMapper {

    @Override
    public void register(MapperFactory factory) {
        factory.classMap(Student.class, BaseStudentDto.class)
                .customize(new ma.glasnost.orika.CustomMapper<Student, BaseStudentDto>() {
                    @Override
                    public void mapAtoB(Student student, BaseStudentDto dto, MappingContext context) {
                        dto.setBirthDate(student.getDateProperty(PropertyName.BIRTH_DATE));
                        dto.setAge(AgeService.getAge(student.getDateProperty(PropertyName.BIRTH_DATE)));
                        dto.setMobile(student.getStringProperty(PropertyName.MOBILE_PHONE));
                        dto.setPersonId(student.getStringProperty(PropertyName.PERSONAL_NUMBER));

                    }

                    @Override
                    public void mapBtoA(BaseStudentDto dto, Student student, MappingContext context) {
                        student.addProperty(PropertyName.MOBILE_PHONE, dto.getMobile());
                        student.addProperty(PropertyName.PERSONAL_NUMBER, dto.getPersonId());
                        student.addProperty(PropertyName.BIRTH_DATE, dto.getBirthDate());
                    }
                })
                .byDefault()
                .register();

    }

}
