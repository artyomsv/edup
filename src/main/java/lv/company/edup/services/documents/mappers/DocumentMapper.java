package lv.company.edup.services.documents.mappers;

import lv.company.edup.infrastructure.mapping.CustomMapper;
import lv.company.edup.persistence.documents.StudentDocumentDetails;
import lv.company.edup.services.documents.dto.StudentDocumentDto;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import org.apache.commons.io.FileUtils;

public class DocumentMapper implements CustomMapper {

    @Override
    public void register(MapperFactory factory) {
        factory.classMap(StudentDocumentDetails.class, StudentDocumentDto.class)
                .customize(new ma.glasnost.orika.CustomMapper<StudentDocumentDetails, StudentDocumentDto>() {
                    @Override
                    public void mapAtoB(StudentDocumentDetails entity, StudentDocumentDto dto, MappingContext context) {
                        dto.setSize(FileUtils.byteCountToDisplaySize(entity.getSize()));

                    }
                })
                .byDefault()
                .register();
    }

}
