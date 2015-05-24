package lv.company.edup.infrastructure.mapping;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class ObjectMapper {

    @Inject MappersProvider provider;

    private MapperFacade facade;

    @PostConstruct
    public void init() {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();
        ConverterFactory converterFactory = factory.getConverterFactory();

        for (CustomConverter converter : provider.getConverters()) {
            converterFactory.registerConverter(converter);
        }
        for (BidirectionalConverter converter : provider.getBiConverters()) {
            converterFactory.registerConverter(converter);
        }
        for (CustomMapper mapper : provider.getMappers()) {
            mapper.register(factory);
        }

        facade = factory.getMapperFacade();
    }

    public void map(Object o1, Object o2) {
        facade.map(o1, o2);
    }

    public <T> T map(Object source, Class<T> clazz) {
        return facade.map(source, clazz);
    }

    public <S, D> List<D> map(Iterable<S> source, Class<D> clazz) {
        return facade.mapAsList(source, clazz);
    }
}
