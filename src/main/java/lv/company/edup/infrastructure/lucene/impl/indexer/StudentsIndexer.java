package lv.company.edup.infrastructure.lucene.impl.indexer;

import lv.company.edup.infrastructure.lucene.api.config.IndexConfigProvider;
import lv.company.edup.infrastructure.lucene.api.config.IndexType;
import lv.company.edup.infrastructure.lucene.api.config.StudentWriter;
import lv.company.edup.infrastructure.lucene.api.indexer.AbstractIndexer;
import lv.company.edup.infrastructure.lucene.impl.LuceneDocumentBuilder;
import lv.company.edup.infrastructure.mapping.ObjectMapper;
import lv.company.edup.persistence.students.current.CurrentStudentVersion;
import lv.company.edup.persistence.students.current.CurrentStudentVersionRepository;
import lv.company.edup.persistence.students.current.CurrentStudentVersion_;
import lv.company.edup.services.students.StudentsService;
import lv.company.edup.services.students.dto.StudentDto;
import org.apache.lucene.index.IndexWriter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
@StudentWriter
public class StudentsIndexer extends AbstractIndexer<StudentDto> {

    private static final Logger LOGGER = Logger.getLogger(StudentsIndexer.class.getSimpleName());

    @Inject CurrentStudentVersionRepository repository;
    @Inject ObjectMapper mapper;
    @Inject IndexConfigProvider configProvider;
    @Inject StudentsService service;

    @Override
    public Collection<StudentDto> load() {
        List<CurrentStudentVersion> versions = repository.findAll();
        service.fetchBaseStudentProperties(versions);
        return mapper.map(versions, StudentDto.class);
    }

    @Override
    public Collection<StudentDto> load(Collection<Long> ids) {
        List<CurrentStudentVersion> list = repository.findByAttribute(ids, CurrentStudentVersion_.id);
        service.fetchBaseStudentProperties(list);
        return mapper.map(list, StudentDto.class);
    }

    @Override
    public IndexType getType() {
        return IndexType.STUDENT;
    }

    @Override
    public IndexWriter getIndexWriter() throws IOException {
        return configProvider.buildWriter(IndexType.STUDENT);
    }

    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    @Override
    public String getId(StudentDto studentDto) {
        return String.valueOf(studentDto.getId());
    }

    @Override
    public LuceneDocumentBuilder prepareDocumentBuilder(StudentDto dto, boolean create) {
        LuceneDocumentBuilder builder = LuceneDocumentBuilder.get();

        builder.add(StudentIndexAttribute.ID, dto.getId(), create);
        builder.add(StudentIndexAttribute.NAME, dto.getName(), create);
        builder.add(StudentIndexAttribute.LAST_NAME, dto.getLastName(), create);
        builder.add(StudentIndexAttribute.PERSON_ID, dto.getPersonId(), create);
        builder.add(StudentIndexAttribute.MAIL, dto.getMail(), create);
        builder.add(StudentIndexAttribute.BIRTH_DATE, dto.getBirthDate(), create);
        builder.add(StudentIndexAttribute.CREATED, dto.getCreated(), create);
        builder.add(StudentIndexAttribute.MOBILE, dto.getMobile(), create);

        builder.addFullTextSearch(configProvider.getFullTextSearchAttributes(getType()), create);

        return builder;
    }

}
