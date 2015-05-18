package lv.company.edup.infrastructure.mapping;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.converter.BidirectionalConverter;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

@ApplicationScoped
public class TransformersProvider {

    @Inject Instance<CustomMapper> mappers;
    @Inject Instance<CustomConverter<?, ?>> converters;
    @Inject Instance<BidirectionalConverter<?, ?>> biConverters;

    public Iterable<CustomMapper> getMappers() {
        return mappers;
    }

    public Iterable<CustomConverter<?, ?>> getConverters() {
        return converters;
    }

    public Iterable<BidirectionalConverter<?, ?>> getBiConverters() {
        return biConverters;
    }
}
