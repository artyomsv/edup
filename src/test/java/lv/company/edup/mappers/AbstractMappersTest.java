package lv.company.edup.mappers;

import lv.company.edup.infrastructure.mapping.CustomMapper;
import lv.company.edup.infrastructure.mapping.MappersProvider;
import lv.company.edup.infrastructure.mapping.ObjectMapper;
import lv.company.edup.services.students.mappers.BaseStudentMapper;
import lv.company.edup.services.students.mappers.StudentMapper;
import lv.company.edup.services.subjects.mappers.EventDetailsMapper;
import lv.company.edup.services.subjects.mappers.EventMapper;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.converter.BidirectionalConverter;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractMappersTest {

    @Mock MappersProvider provider;
    @InjectMocks ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {

        Mockito.when(provider.getMappers()).thenReturn(getMappers());
        Mockito.when(provider.getConverters()).thenReturn(getConverters());
        Mockito.when(provider.getBiConverters()).thenReturn(getBiConverters());

        mapper.init();
    }

    private Iterable<BidirectionalConverter<?, ?>> getBiConverters() {
        return new ArrayList<>();
    }

    private Iterable<CustomConverter<?, ?>> getConverters() {
        return new ArrayList<>();
    }

    private Iterable<CustomMapper> getMappers() {
        ArrayList<CustomMapper> customMappers = new ArrayList<>();
        customMappers.add(new BaseStudentMapper());
        customMappers.add(new StudentMapper());
        customMappers.add(new EventMapper());
        customMappers.add(new EventDetailsMapper());
        return customMappers;
    }
}
