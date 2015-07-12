package lv.company.edup.infrastructure.lucene.impl.indexer;

import lv.company.edup.infrastructure.lucene.api.config.IndexConfigProvider;
import lv.company.edup.infrastructure.lucene.api.config.IndexType;
import lv.company.edup.infrastructure.lucene.api.indexer.AbstractIndexer;
import lv.company.edup.infrastructure.lucene.api.indexer.SubjectWriter;
import lv.company.edup.infrastructure.lucene.impl.LuceneDocumentBuilder;
import lv.company.edup.infrastructure.mapping.ObjectMapper;
import lv.company.edup.persistence.subjects.SubjectRepository;
import lv.company.edup.persistence.subjects.domain.Subject;
import lv.company.edup.persistence.subjects.domain.Subject_;
import lv.company.edup.services.students.StudentsService;
import lv.company.edup.services.subjects.dto.SubjectDto;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
@SubjectWriter
public class SubjectsIndexWriter extends AbstractIndexer<SubjectDto> {

    private static final Logger LOGGER = Logger.getLogger(SubjectsIndexWriter.class.getSimpleName());

    @Inject SubjectRepository repository;
    @Inject ObjectMapper mapper;
    @Inject IndexConfigProvider configProvider;
    @Inject StudentsService service;

    @Override
    public Collection<SubjectDto> load() {
        List<Subject> versions = repository.findAll();
        return mapper.map(versions, SubjectDto.class);
    }

    @Override
    public Collection<SubjectDto> load(Collection<Long> ids) {
        List<Subject> list = repository.findByAttribute(ids, Subject_.subjectId);
        return mapper.map(list, SubjectDto.class);
    }

    @Override
    public IndexType getType() {
        return IndexType.SUBJECT;
    }

    @Override
    public IndexWriter getIndexWriter() throws IOException {
        return configProvider.buildWriter(IndexType.SUBJECT);
    }

    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    @Override
    public String getId(SubjectDto dto) {
        return String.valueOf(dto.getSubjectId());
    }

    @Override
    public Document build(SubjectDto dto) {
        LuceneDocumentBuilder builder = LuceneDocumentBuilder.get();

        builder.add(SubjectsIndexAttribute.ID, dto.getSubjectId());
        builder.add(SubjectsIndexAttribute.NAME, dto.getSubjectName());
        builder.add(SubjectsIndexAttribute.CREATED, dto.getCreated());

        builder.addFullTextSearch(configProvider.getFullTextSearchAttributes(getType()));

        return builder.build();
    }

}
