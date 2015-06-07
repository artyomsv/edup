package lv.company.edup.infrastructure.lucene.api.indexer;

import lv.company.edup.infrastructure.lucene.api.config.IndexType;
import lv.company.edup.infrastructure.lucene.impl.LuceneDocumentBuilder;

import java.io.IOException;
import java.util.Collection;
import java.util.logging.Logger;

public interface Indexer<T> {

    void rebuild(Collection<T> collection);

    void add(Collection<T> t);

    void add(T t);

    void update(T t);

    void delete(Long id);

    Collection<T> load();

    Collection<T> load(Collection<Long> ids);


    IndexType getType();

    org.apache.lucene.index.IndexWriter getIndexWriter() throws IOException;

    Logger getLogger();

    String getId(T t);

    LuceneDocumentBuilder prepareDocumentBuilder(T t, boolean create);

}
