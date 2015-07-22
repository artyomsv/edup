package lv.company.edup.infrastructure.lucene.api.indexer;

import lv.company.edup.infrastructure.lucene.impl.LuceneDocumentUtils;
import org.apache.commons.io.IOUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;
import java.util.Collection;

public abstract class AbstractIndexer<T> implements Indexer<T> {

    @Override
    public void add(T t) {
        IndexWriter indexWriter = null;
        try {
            indexWriter = getIndexWriter();
            indexRecord(t, indexWriter);
        } catch (IOException e) {
            e.printStackTrace();
            IOUtils.closeQuietly(indexWriter);
        } finally {
            IOUtils.closeQuietly(indexWriter);
        }
    }

    @Override
    public void add(Collection<T> collection) {
        IndexWriter indexWriter = null;

        try {
            indexWriter = getIndexWriter();
            for (T t : collection) {
                indexRecord(t, indexWriter);
            }
        } catch (IOException e) {
            e.printStackTrace();
            IOUtils.closeQuietly(indexWriter);
        } finally {
            IOUtils.closeQuietly(indexWriter);
        }

    }

    @Override
    public void delete(Long id) {
        IndexWriter indexWriter = null;
        try {
            indexWriter = getIndexWriter();
            indexWriter.deleteDocuments(new Term(LuceneDocumentUtils.TECHNICAL_ID, String.valueOf(id)));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(indexWriter);
        }
    }

    @Override
    public void delete() {
        IndexWriter indexWriter = null;
        try {
            indexWriter = getIndexWriter();
            indexWriter.deleteAll();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(indexWriter);
        }
    }

    @Override
    public void fullRebuild() {
        delete();
        add(load());
    }

    private void indexRecord(T t, IndexWriter indexWriter) throws IOException {
        Document document = build(t);

        String id = getId(t);
        document.add(new NumericDocValuesField(LuceneDocumentUtils.TECHNICAL_ID, Long.valueOf(id)));
        document.add(new StringField(LuceneDocumentUtils.TECHNICAL_ID, id, Field.Store.YES));
        document.add(new SortedDocValuesField(LuceneDocumentUtils.getSortableField(LuceneDocumentUtils.TECHNICAL_ID), new BytesRef(id)));

        indexWriter.updateDocument(new Term(LuceneDocumentUtils.TECHNICAL_ID, id), document);
    }

}
