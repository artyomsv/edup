package lv.company.edup.infrastructure.mapping;

import ma.glasnost.orika.MapperFactory;

public interface CustomMapper {

    void register(MapperFactory factory);

}
