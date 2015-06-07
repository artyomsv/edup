package lv.company.edup.infrastructure.lucene.api.indexer;

import lv.company.edup.infrastructure.lucene.api.config.IndexType;
import org.apache.lucene.document.Document;

import java.io.IOException;
import java.util.Collection;
import java.util.logging.Logger;

public interface Indexer<T> {

    void add(Collection<T> t);

    void add(T t);

    void delete(Long id);

    Collection<T> load();

    Collection<T> load(Collection<Long> ids);


    IndexType getType();

    org.apache.lucene.index.IndexWriter getIndexWriter() throws IOException;

    Logger getLogger();

    String getId(T t);

    Document build(T t);

}
