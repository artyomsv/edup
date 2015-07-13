package lv.company.edup.infrastructure.lucene.impl.searcher;

import lv.company.edup.infrastructure.lucene.api.config.AppAnalyzer;
import lv.company.edup.infrastructure.lucene.api.config.IndexConfigProvider;
import lv.company.edup.infrastructure.lucene.api.config.IndexType;
import lv.company.edup.infrastructure.lucene.api.indexer.IndexAttribute;
import lv.company.edup.infrastructure.lucene.api.searcher.AbstractSearcher;
import lv.company.edup.infrastructure.lucene.api.searcher.SubjectSearcher;
import lv.company.edup.infrastructure.mapping.ObjectMapper;
import lv.company.edup.persistence.subjects.SubjectRepository;
import lv.company.edup.persistence.subjects.domain.Subject;
import lv.company.edup.persistence.subjects.domain.Subject_;
import lv.company.edup.services.subjects.dto.SubjectDto;
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

import static lv.company.edup.infrastructure.lucene.impl.indexer.StudentIndexAttribute.CREATED;
import static lv.company.edup.infrastructure.lucene.impl.indexer.StudentIndexAttribute.ID;

@SubjectSearcher
@ApplicationScoped
public class SubjectIndexSearcher extends AbstractSearcher<SubjectDto> {

    private static final Logger LOGGER = Logger.getLogger(SubjectIndexSearcher.class.getSimpleName());

    private Analyzer analyzer = new AppAnalyzer();

    @Inject IndexConfigProvider provider;
    @Inject SubjectRepository repository;
    @Inject ObjectMapper mapper;
    @Inject QueryParser parser;

    @Override
    public List<SubjectDto> load(Collection<Long> ids) {
        List<Subject> list = repository.findByAttribute(ids, Subject_.subjectId);
        return mapper.map(list, SubjectDto.class);
    }

    @Override
    public IndexType getType() {
        return IndexType.SUBJECT;
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
        return Arrays.asList(ID.getValue(), CREATED.getValue());
    }

    @Override
    public QueryParser getQueryParser() {
        return parser;
    }

    @Override
    public Long getId(SubjectDto dto) {
        return dto != null ? dto.getSubjectId() : null;
    }
}
