package lv.company.edup.infrastructure.lucene.api.indexer;

import lv.company.edup.infrastructure.lucene.api.config.IndexType;
import org.apache.lucene.document.Document;

import java.io.IOException;
import java.util.Collection;
import java.util.logging.Logger;

public interface Indexer<T> {

    void add(Collection<T> collection);

    void add(T t);

    void delete(Long id);

    void delete(Collection<Long> ids);

    void rebuild();

    Collection<T> load();

    Collection<T> load(Collection<Long> ids);

    Document build(T t);

    IndexType getType();

    org.apache.lucene.index.IndexWriter getIndexWriter() throws IOException;

    Logger getLogger();

    String getId(T t);

}
