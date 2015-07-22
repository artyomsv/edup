package lv.company.edup.lucene;

import lv.company.edup.infrastructure.lucene.api.config.IndexConfigProvider;
import lv.company.edup.infrastructure.lucene.impl.indexer.StudentsIndexWriter;
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
import org.apache.commons.lang3.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
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
import java.util.Date;
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

    @InjectMocks StudentsIndexWriter indexer;
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
    @Ignore
    public void filterNoEqualsSingleId() throws Exception {
        MultivaluedMap<String, String> parameters = new MultivaluedHashMap<String, String>();
        parameters.add("$filter", "Id ne 1");
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
    public void filterMultipleIdsOrderByCreatedAsc() throws Exception {
        MultivaluedMap<String, String> parameters = new MultivaluedHashMap<String, String>();
        parameters.add("$filter", "Id eq (2,3,12)");
        parameters.add("$orderby", "Created asc");
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
    public void filterMultipleIdsOrderByCreatedDesc() throws Exception {
        MultivaluedMap<String, String> parameters = new MultivaluedHashMap<String, String>();
        parameters.add("$filter", "Id eq (2,3,12)");
        parameters.add("$orderby", "Created desc");
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

    @Test
    public void filterOrderCreatedAsc() throws Exception {
        MultivaluedMap<String, String> parameters = new MultivaluedHashMap<String, String>();
        parameters.add("$orderby", "Created asc");
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

    @Test
    public void filterOrderCreatedDesc() throws Exception {
        MultivaluedMap<String, String> parameters = new MultivaluedHashMap<String, String>();
        parameters.add("$orderby", "Created desc");
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
    public void filterOrderByCreatedDesc_AddNewRecordAfter() throws Exception {
        MultivaluedMap<String, String> parameters = new MultivaluedHashMap<String, String>();
        parameters.add("$orderby", "Created desc");
        parameters.add("$count", "true");
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

        db.put(21L, getStudentDto(21L, "Brtyom", "Stukans", DateUtils.addDays(new Date(), 40)));
        indexer.add(db.get(21L));


        result = searcher.search(criteria);
        assertThat(result.getCount(), is(21L));
        assertThat(result.getValues().iterator().next().getId(), is(21L));

        indexer.add(db.get(21L));

        criteria.setTop(999);
        result = searcher.search(criteria);
        assertThat(result.getCount(), is(21L));
        iterator = result.getValues().iterator();
        assertThat(iterator.next().getId(), is(21L));
        assertThat(iterator.next().getId(), is(20L));

        db.get(20L).setName("Artyom");
        indexer.add(db.get(20L));

        parameters = new MultivaluedHashMap<String, String>();
        parameters.add("$orderby", "Name asc");
        parameters.add("$count", "true");
        parameters.add("$all", "true");
        criteria = new ODataCriteria(parameters);
        criteria.setSearch("*");
        result = searcher.search(criteria);
        assertThat(result.getCount(), is(21L));
        iterator = result.getValues().iterator();
        assertThat(iterator.next().getId(), is(20L));
        assertThat(iterator.next().getId(), is(21L));
    }

    private StudentDto getStudentDto(long id, String name, String lastName, Date created) {
        StudentDto studentDto = new StudentDto();
        studentDto.setId(id);
        studentDto.setName(name);
        studentDto.setLastName(lastName);
        studentDto.setCreated(created);
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
        Date date = new Date();
        int day = 0;
        db.put(1L, getStudentDto(1L, "z", "A", DateUtils.addDays(date, day++)));
        db.put(2L, getStudentDto(2L, "y", "B", DateUtils.addDays(date, day++)));
        db.put(3L, getStudentDto(3L, "x", "C", DateUtils.addDays(date, day++)));
        db.put(4L, getStudentDto(4L, "w", "D", DateUtils.addDays(date, day++)));
        db.put(5L, getStudentDto(5L, "v", "E", DateUtils.addDays(date, day++)));
        db.put(6L, getStudentDto(6L, "u", "F", DateUtils.addDays(date, day++)));
        db.put(7L, getStudentDto(7L, "t", "G", DateUtils.addDays(date, day++)));
        db.put(8L, getStudentDto(8L, "s", "H", DateUtils.addDays(date, day++)));
        db.put(9L, getStudentDto(9L, "r", "I", DateUtils.addDays(date, day++)));
        db.put(10L, getStudentDto(10L, "q", "J", DateUtils.addDays(date, day++)));
        db.put(11L, getStudentDto(11L, "p", "K", DateUtils.addDays(date, day++)));
        db.put(12L, getStudentDto(12L, "o", "L", DateUtils.addDays(date, day++)));
        db.put(13L, getStudentDto(13L, "n", "M", DateUtils.addDays(date, day++)));
        db.put(14L, getStudentDto(14L, "m", "N", DateUtils.addDays(date, day++)));
        db.put(15L, getStudentDto(15L, "l", "O", DateUtils.addDays(date, day++)));
        db.put(16L, getStudentDto(16L, "k", "P", DateUtils.addDays(date, day++)));
        db.put(17L, getStudentDto(17L, "j", "Q", DateUtils.addDays(date, day++)));
        db.put(18L, getStudentDto(18L, "i", "R", DateUtils.addDays(date, day++)));
        db.put(19L, getStudentDto(19L, "h", "S", DateUtils.addDays(date, day++)));
        db.put(20L, getStudentDto(20L, "g", "T", DateUtils.addDays(date, day++)));
    }
}
