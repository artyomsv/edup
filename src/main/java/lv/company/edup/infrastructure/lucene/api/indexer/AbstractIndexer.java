package lv.company.edup.infrastructure.lucene.api.indexer;

import lv.company.edup.infrastructure.lucene.impl.LuceneDocumentBuilder;
import lv.company.edup.infrastructure.lucene.impl.LuceneDocumentUtils;
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
    public void update(T t) {
        IndexWriter indexWriter = null;
        try {
            LuceneDocumentBuilder builder = prepareDocumentBuilder(t, false);
            Document document = builder.build();

            String id = getId(t);
            document.add(new LongField(LuceneDocumentUtils.TECHNICAL_ID, Long.valueOf(id), Field.Store.YES));

            indexWriter = getIndexWriter();
            Term term = new Term(LuceneDocumentUtils.TECHNICAL_ID, id);
            indexWriter.updateDocument(term, document);

            indexWriter.updateNumericDocValue(term, LuceneDocumentUtils.TECHNICAL_ID, Long.valueOf(id));
            indexWriter.updateNumericDocValue(term, LuceneDocumentUtils.getSortableField(LuceneDocumentUtils.TECHNICAL_ID), Long.valueOf(id));


        } catch (IOException e) {
            e.printStackTrace();
            IOUtils.closeQuietly(indexWriter);
        } finally {
            IOUtils.closeQuietly(indexWriter);
        }

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void rebuild(Collection<T> collection) {

    }

    private void indexRecord(T t, IndexWriter indexWriter) throws IOException {
        LuceneDocumentBuilder builder = prepareDocumentBuilder(t, true);
        Document document = builder.build();

        String id = getId(t);
        document.add(new NumericDocValuesField(LuceneDocumentUtils.TECHNICAL_ID, Long.valueOf(id)));
        document.add(new LongField(LuceneDocumentUtils.TECHNICAL_ID, Long.valueOf(id), Field.Store.YES));
        document.add(new SortedNumericDocValuesField(LuceneDocumentUtils.getSortableField(LuceneDocumentUtils.TECHNICAL_ID), Long.valueOf(id)));

        indexWriter.updateDocument(new Term(LuceneDocumentUtils.TECHNICAL_ID, id), document);
    }

}
