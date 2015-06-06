package lv.company.edup.infrastructure.lucene.api.indexer;

import lv.company.edup.infrastructure.exceptions.InternalException;
import lv.company.edup.infrastructure.lucene.impl.LuceneDocumentUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.SortedNumericDocValuesField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;

import java.io.IOException;
import java.util.Collection;

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
                    String id = getId(t);
                    indexWriter.updateDocument(new Term(LuceneDocumentUtils.TECHNICAL_ID, id), prepareDocument(t));
                }
            } catch (IOException e) {
                IOUtils.closeQuietly(indexWriter);
                throw new InternalException(e);
            } finally {
                IOUtils.closeQuietly(indexWriter);
            }
        }
    }

    private Document prepareDocument(T t) {
        Document document = build(t);
        String id = getId(t);
//        document.add(new StringField(LuceneDocumentUtils.TECHNICAL_ID, id, Store.YES));
//      document.add(new BinaryDocValuesField(LuceneDocumentUtils.TECHNICAL_ID, new BytesRef(id)));
//        document.add(new SortedDocValuesField(LuceneDocumentUtils.getSortableField(LuceneDocumentUtils.TECHNICAL_ID), new BytesRef(id)));

        document.add(new NumericDocValuesField(LuceneDocumentUtils.TECHNICAL_ID, Long.valueOf(id)));
        document.add(new LongField(LuceneDocumentUtils.TECHNICAL_ID, Long.valueOf(id), Field.Store.YES));
        document.add(new SortedNumericDocValuesField(LuceneDocumentUtils.getSortableField(LuceneDocumentUtils.TECHNICAL_ID), Long.valueOf(id)));
        return document;
    }

    @Override
    public void add(T t) {
        IndexWriter indexWriter = null;
        try {
            if (t != null) {
                String id = getId(t);
                indexWriter = getIndexWriter();
                indexWriter.updateDocument(new Term(LuceneDocumentUtils.TECHNICAL_ID), prepareDocument(t));
            }
        } catch (IOException e) {
            IOUtils.closeQuietly(indexWriter);
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
