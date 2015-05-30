package lv.company.edup.infrastructure.lucene.api.indexer;

import lv.company.edup.infrastructure.exceptions.InternalException;
import lv.company.edup.infrastructure.lucene.impl.LuceneDocumentUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;

import java.io.IOException;
import java.util.Collection;

import static org.apache.lucene.document.Field.Store;

public abstract class AbstractIndexer<T> implements Indexer<T> {

    @Override
    public void rebuild() {
        add(load());
    }

    @Override
    public void add(Collection<T> collection) {
        if (CollectionUtils.isNotEmpty(collection)) {
            IndexWriter indexWriter = null;
            try {

                indexWriter = getIndexWriter();
                for (T t : collection) {
                    Document document = build(t);
                    String id = getId(t);
                    document.add(new StringField(LuceneDocumentUtils.TECHNICAL_ID, id, Store.YES));
                    indexWriter.updateDocument(new Term(LuceneDocumentUtils.TECHNICAL_ID, id), document);
                }
            } catch (IOException e) {
                IOUtils.closeQuietly(indexWriter);
                throw new InternalException(e);
            } finally {
                IOUtils.closeQuietly(indexWriter);
            }
        }
    }

    @Override
    public void add(T t) {
        IndexWriter indexWriter = null;
        try {
            if (t != null) {
                Document document = build(t);
                indexWriter = getIndexWriter();
                indexWriter.updateDocument(new Term(LuceneDocumentUtils.TECHNICAL_ID), document);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(indexWriter);
        }
    }

    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Collection<Long> ids) {
        throw new UnsupportedOperationException();
    }


}
