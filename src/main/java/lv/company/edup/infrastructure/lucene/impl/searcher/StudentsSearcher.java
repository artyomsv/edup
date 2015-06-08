package lv.company.edup.infrastructure.lucene.impl.searcher;

import lv.company.edup.infrastructure.lucene.api.config.AppAnalyzer;
import lv.company.edup.infrastructure.lucene.api.config.IndexConfigProvider;
import lv.company.edup.infrastructure.lucene.api.config.IndexType;
import lv.company.edup.infrastructure.lucene.api.config.StudentReader;
import lv.company.edup.infrastructure.lucene.api.indexer.IndexAttribute;
import lv.company.edup.infrastructure.lucene.api.searcher.AbstractSearcher;
import lv.company.edup.infrastructure.mapping.ObjectMapper;
import lv.company.edup.persistence.students.current.CurrentStudentVersion;
import lv.company.edup.persistence.students.current.CurrentStudentVersionRepository;
import lv.company.edup.persistence.students.current.CurrentStudentVersion_;
import lv.company.edup.services.students.StudentsService;
import lv.company.edup.services.students.dto.StudentDto;
import lv.company.odata.impl.parse.QueryParser;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.IndexSearcher;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static lv.company.edup.infrastructure.lucene.impl.indexer.StudentIndexAttribute.BIRTH_DATE;
import static lv.company.edup.infrastructure.lucene.impl.indexer.StudentIndexAttribute.CREATED;
import static lv.company.edup.infrastructure.lucene.impl.indexer.StudentIndexAttribute.ID;
import static lv.company.edup.infrastructure.lucene.impl.indexer.StudentIndexAttribute.UPDATED;

@StudentReader
@ApplicationScoped
public class StudentsSearcher extends AbstractSearcher<StudentDto> {

    private static final Logger LOGGER = Logger.getLogger(StudentsSearcher.class.getSimpleName());

    private Analyzer analyzer = new AppAnalyzer();

    @Inject IndexConfigProvider provider;
    @Inject CurrentStudentVersionRepository repository;
    @Inject ObjectMapper mapper;
    @Inject QueryParser parser;
    @Inject StudentsService service;

    @Override
    public List<StudentDto> load(Collection<Long> ids) {
        List<CurrentStudentVersion> list = repository.findByAttribute(ids, CurrentStudentVersion_.id);
        service.fetchBaseStudentProperties(list);
        return mapper.map(list, StudentDto.class);
    }

    @Override
    public IndexType getType() {
        return IndexType.STUDENT;
    }

    @Override
    public IndexSearcher getIndexSearcher() {
        return provider.getSearcher(getType());
    }

    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    @Override
    public Analyzer getAnalyzer() {
        return analyzer;
    }

    @Override
    public Collection<? extends IndexAttribute> getAttributes() {
        return provider.getFullTextSearchAttributes(getType());
    }

    @Override
    public Map<IndexAttribute, Float> getCustomBoosts() {
        return Collections.emptyMap();
    }

    @Override
    public Collection<String> getNumericFields() {
        return Arrays.asList(ID.getValue(), CREATED.getValue(), UPDATED.getValue(), BIRTH_DATE.getValue());
    }

    @Override
    public QueryParser getQueryParser() {
        return parser;
    }

    @Override
    public Long getId(StudentDto dto) {
        return dto != null ? dto.getId() : null;
    }
}
