package lv.company.edup.mappers;

import lv.company.edup.infrastructure.lucene.api.config.IndexConfigProvider;
import lv.company.edup.infrastructure.lucene.impl.indexer.StudentsIndexer;
import lv.company.edup.infrastructure.lucene.impl.searcher.StudentsSearcher;
import lv.company.edup.infrastructure.mapping.CustomMapper;
import lv.company.edup.infrastructure.mapping.MappersProvider;
import lv.company.edup.infrastructure.mapping.ObjectMapper;
import lv.company.edup.persistence.students.current.CurrentStudentVersion;
import lv.company.edup.persistence.students.current.CurrentStudentVersionRepository;
import lv.company.edup.persistence.students.current.CurrentStudentVersion_;
import lv.company.edup.services.students.StudentsService;
import lv.company.edup.services.students.dto.StudentDto;
import lv.company.edup.services.students.mappers.BaseStudentMapper;
import lv.company.edup.services.students.mappers.StudentMapper;
import lv.company.odata.api.ODataCriteria;
import lv.company.odata.api.ODataResult;
import lv.company.odata.impl.parse.QueryParserImpl;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.converter.BidirectionalConverter;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import javax.persistence.metamodel.SingularAttribute;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.anyCollectionOf;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LuceneTest {

    @Mock CurrentStudentVersionRepository repository;
    @Mock StudentsService service;
    @Mock MappersProvider provider;

    @Spy ObjectMapper mapper;
    @Spy IndexConfigProvider configProvider;
    @Spy QueryParserImpl parser;

    @InjectMocks StudentsIndexer indexer;
    @InjectMocks StudentsSearcher searcher;

    private Map<Long, StudentDto> db = new HashedMap();

    @BeforeClass
    public static void testSetUp() {

    }

    @Before
    public void setUp() throws Exception {
        when(provider.getMappers()).thenReturn(getMappers());
        when(provider.getConverters()).thenReturn(getConverters());
        when(provider.getBiConverters()).thenReturn(getBiConverters());
        mapper.setProvider(provider);
        mapper.init();

        configProvider.init();

        when(repository.findByAttribute(anyCollectionOf(Long.class), (SingularAttribute<CurrentStudentVersion, Long>) any(CurrentStudentVersion_.class))).thenAnswer(new Answer<Collection<StudentDto>>() {
            @Override
            public Collection<StudentDto> answer(InvocationOnMock invocationOnMock) throws Throwable {
                Object[] arguments = invocationOnMock.getArguments();
                Collection<StudentDto> result = new ArrayList<StudentDto>();
                Collection<Long> ids = (Collection<Long>) arguments[0];

                for (Long id : ids) {
                    result.add(db.get(id));
                }
                return result;
            }
        });

        init();

        Collection<StudentDto> values1 = db.values();
        indexer.add(values1);
    }

    @After
    public void tearDown() throws Exception {
        Path path = FileSystems.getDefault().getPath("index");
        File indexDirectory = path.toFile();
        FileUtils.forceDelete(indexDirectory);
    }

    @Test
    public void simpleTestNoParameters() throws Exception {
        MultivaluedMap<String, String> parameters = new MultivaluedHashMap<String, String>();
        ODataCriteria criteria = new ODataCriteria(parameters);
        criteria.setSearch("*");


        ODataResult<StudentDto> dataResult = searcher.search(criteria);
        List<StudentDto> values = dataResult.getValues();
        assertNotNull(values);
        assertThat(values.size(), is(10));
    }

    @Test
    public void filterSingleId() throws Exception {
        MultivaluedMap<String, String> parameters = new MultivaluedHashMap<String, String>();
        parameters.add("$filter", "Id eq 1");
        ODataCriteria criteria = new ODataCriteria(parameters);

        ODataResult<StudentDto> result = searcher.search(criteria);
        List<StudentDto> values = result.getValues();
        assertThat(values.size(), is(1));
        Iterator<StudentDto> iterator = values.iterator();
        assertThat(iterator.next().getId(), is(1L));
    }

    @Test
    public void filterMultipleIds() throws Exception {
        MultivaluedMap<String, String> parameters = new MultivaluedHashMap<String, String>();
        parameters.add("$filter", "Id eq (1, 5)");
        ODataCriteria criteria = new ODataCriteria(parameters);

        ODataResult<StudentDto> result = searcher.search(criteria);
        List<StudentDto> values = result.getValues();
        assertThat(values.size(), is(2));
        Iterator<StudentDto> iterator = values.iterator();
        assertThat(iterator.next().getId(), is(1L));
        assertThat(iterator.next().getId(), is(5L));
    }

    @Test
    public void filterMultipleIdsOrName() throws Exception {
        MultivaluedMap<String, String> parameters = new MultivaluedHashMap<String, String>();
        parameters.add("$filter", "Id eq (3,2) or Name eq 'o'");
        ODataCriteria criteria = new ODataCriteria(parameters);

        ODataResult<StudentDto> result = searcher.search(criteria);
        List<StudentDto> values = result.getValues();
        assertThat(values.size(), is(3));
        Iterator<StudentDto> iterator = values.iterator();
        assertThat(iterator.next().getId(), is(2L));
        assertThat(iterator.next().getId(), is(3L));
        assertThat(iterator.next().getId(), is(12L));
    }

    @Test
    public void filterMultipleIdsOrNameOrderByIdAsc() throws Exception {
        MultivaluedMap<String, String> parameters = new MultivaluedHashMap<String, String>();
        parameters.add("$filter", "Id eq (2,3) or Name eq 'o'");
        parameters.add("$orderby", "Id asc");
        ODataCriteria criteria = new ODataCriteria(parameters);

        ODataResult<StudentDto> result = searcher.search(criteria);
        List<StudentDto> values = result.getValues();
        assertThat(values.size(), is(3));
        Iterator<StudentDto> iterator = values.iterator();
        assertThat(iterator.next().getId(), is(2L));
        assertThat(iterator.next().getId(), is(3L));
        assertThat(iterator.next().getId(), is(12L));
    }

    @Test
    public void filterMultipleIdsOrNameOrderByIdDesc() throws Exception {
        MultivaluedMap<String, String> parameters = new MultivaluedHashMap<String, String>();
        parameters.add("$filter", "Id eq (2,3) or Name eq 'o'");
        parameters.add("$orderby", "Id desc");
        ODataCriteria criteria = new ODataCriteria(parameters);

        ODataResult<StudentDto> result = searcher.search(criteria);
        List<StudentDto> values = result.getValues();
        assertThat(values.size(), is(3));
        Iterator<StudentDto> iterator = values.iterator();
        assertThat(iterator.next().getId(), is(12L));
        assertThat(iterator.next().getId(), is(3L));
        assertThat(iterator.next().getId(), is(2L));
    }

    @Test
    public void filterMultipleIdsOrNameOrderByNameDesc() throws Exception {
        MultivaluedMap<String, String> parameters = new MultivaluedHashMap<String, String>();
        parameters.add("$filter", "Id eq (2,3) or Name eq 'o'");
        parameters.add("$orderby", "Name desc");
        ODataCriteria criteria = new ODataCriteria(parameters);

        ODataResult<StudentDto> result = searcher.search(criteria);
        List<StudentDto> values = result.getValues();
        assertThat(values.size(), is(3));
        Iterator<StudentDto> iterator = values.iterator();
        assertThat(iterator.next().getId(), is(2L));
        assertThat(iterator.next().getId(), is(3L));
        assertThat(iterator.next().getId(), is(12L));
    }

    @Test
    public void filterMultipleIdsOrNameOrderByNameAsc() throws Exception {
        MultivaluedMap<String, String> parameters = new MultivaluedHashMap<String, String>();
        parameters.add("$filter", "Id eq (2,3) or Name eq 'o'");
        parameters.add("$orderby", "Name asc");
        ODataCriteria criteria = new ODataCriteria(parameters);

        ODataResult<StudentDto> result = searcher.search(criteria);
        List<StudentDto> values = result.getValues();
        assertThat(values.size(), is(3));
        Iterator<StudentDto> iterator = values.iterator();
        assertThat(iterator.next().getId(), is(12L));
        assertThat(iterator.next().getId(), is(3L));
        assertThat(iterator.next().getId(), is(2L));
    }

    @Test
    public void filterOrderByIdDesc() throws Exception {
        MultivaluedMap<String, String> parameters = new MultivaluedHashMap<String, String>();
        parameters.add("$orderby", "Id desc");
        ODataCriteria criteria = new ODataCriteria(parameters);
        criteria.setSearch("*");

        ODataResult<StudentDto> result = searcher.search(criteria);
        List<StudentDto> values = result.getValues();
        assertThat(values.size(), is(10));
        Iterator<StudentDto> iterator = values.iterator();

        long id = 20L;
        for (int i = 0; i < values.size(); i++) {
            assertThat(iterator.next().getId(), is(id--));

        }
    }

    @Test
    public void filterOrderByNameAsc() throws Exception {
        MultivaluedMap<String, String> parameters = new MultivaluedHashMap<String, String>();
        parameters.add("$orderby", "Name asc");
        ODataCriteria criteria = new ODataCriteria(parameters);
        criteria.setSearch("*");

        ODataResult<StudentDto> result = searcher.search(criteria);
        List<StudentDto> values = result.getValues();
        assertThat(values.size(), is(10));
        Iterator<StudentDto> iterator = values.iterator();
        long id = 20L;
        for (int i = 0; i < values.size(); i++) {
            assertThat(iterator.next().getId(), is(id--));

        }
    }

    @Test
    public void filterOrderByNameDesc() throws Exception {
        MultivaluedMap<String, String> parameters = new MultivaluedHashMap<String, String>();
        parameters.add("$orderby", "Name desc");
        ODataCriteria criteria = new ODataCriteria(parameters);
        criteria.setSearch("*");

        ODataResult<StudentDto> result = searcher.search(criteria);
        List<StudentDto> values = result.getValues();
        assertThat(values.size(), is(10));
        Iterator<StudentDto> iterator = values.iterator();
        long id = 1L;
        for (int i = 0; i < values.size(); i++) {
            assertThat(iterator.next().getId(), is(id++));

        }
    }

    private StudentDto getStudentDto(long id, String name, String lastName) {
        StudentDto studentDto = new StudentDto();
        studentDto.setId(id);
        studentDto.setName(name);
        studentDto.setLastName(lastName);
        return studentDto;
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
        return customMappers;
    }

    private void init() {
        db.put(1L, getStudentDto(1L, "z", "A"));
        db.put(2L, getStudentDto(2L, "y", "B"));
        db.put(3L, getStudentDto(3L, "x", "C"));
        db.put(4L, getStudentDto(4L, "w", "D"));
        db.put(5L, getStudentDto(5L, "v", "E"));
        db.put(6L, getStudentDto(6L, "u", "F"));
        db.put(7L, getStudentDto(7L, "t", "G"));
        db.put(8L, getStudentDto(8L, "s", "H"));
        db.put(9L, getStudentDto(9L, "r", "I"));
        db.put(10L, getStudentDto(10L, "q", "J"));
        db.put(11L, getStudentDto(11L, "p", "K"));
        db.put(12L, getStudentDto(12L, "o", "L"));
        db.put(13L, getStudentDto(13L, "n", "M"));
        db.put(14L, getStudentDto(14L, "m", "N"));
        db.put(15L, getStudentDto(15L, "l", "O"));
        db.put(16L, getStudentDto(16L, "k", "P"));
        db.put(17L, getStudentDto(17L, "j", "Q"));
        db.put(18L, getStudentDto(18L, "i", "R"));
        db.put(19L, getStudentDto(19L, "h", "S"));
        db.put(20L, getStudentDto(20L, "g", "T"));
    }
}
