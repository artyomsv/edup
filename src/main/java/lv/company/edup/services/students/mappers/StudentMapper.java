package lv.company.edup.services.students.mappers;

import lv.company.edup.infrastructure.mapping.CustomMapper;
import lv.company.edup.infrastructure.response.UriUtils;
import lv.company.edup.persistence.students.Student;
import lv.company.edup.persistence.students.properties.PropertyName;
import lv.company.edup.services.students.dto.StudentDto;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;

import javax.inject.Inject;

public class StudentMapper implements CustomMapper {

    private static final String SECURED_FILES_DOWNLOAD = "/private/files";
    @Inject UriUtils utils;

    @Override
    public void register(MapperFactory factory) {
        factory.classMap(Student.class, StudentDto.class)
                .customize(new ma.glasnost.orika.CustomMapper<Student, StudentDto>() {
                    @Override
                    public void mapAtoB(Student entity, StudentDto dto, MappingContext context) {
                        dto.setPhotoId(entity.getLongProperty(PropertyName.PHOTO_ID));
                        if (utils != null) {
                            if (dto.getPhotoId() == null) {
                                dto.setPhotoUrl(utils.getRootUrl().replace("/api", "") + "/images/unknown.png");
                            } else {
                                dto.setPhotoUrl(utils.getRootUrl() + SECURED_FILES_DOWNLOAD + "/" + dto.getPhotoId());
                            }
                        }
                        dto.setMail(entity.getStringProperty(PropertyName.MAIL));
                        dto.setParentsInfo(entity.getStringProperty(PropertyName.PARENT_INFORMATION));
                        dto.setCharacteristics(entity.getStringProperty(PropertyName.CHARACTERISTICS));
                    }

                    @Override
                    public void mapBtoA(StudentDto dto, Student student, MappingContext context) {
                        if (dto.getPhotoId() != null) {
                            student.addProperty(PropertyName.PHOTO_ID, dto.getPhotoId());
                        }
                        student.addProperty(PropertyName.MAIL, dto.getMail());
                        student.addProperty(PropertyName.PARENT_INFORMATION, dto.getParentsInfo());
                        student.addProperty(PropertyName.CHARACTERISTICS, dto.getCharacteristics());
                    }
                })
                .byDefault()
                .register();

    }

}
