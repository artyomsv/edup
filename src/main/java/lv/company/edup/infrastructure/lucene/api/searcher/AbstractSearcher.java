package lv.company.edup.infrastructure.lucene.api.searcher;

import lv.company.edup.infrastructure.lucene.impl.LuceneDocumentUtils;
import lv.company.edup.infrastructure.lucene.impl.searcher.LuceneSorterQueryBuilder;
import lv.company.edup.infrastructure.utils.AppCollectionUtils;
import lv.company.odata.api.ODataCriteria;
import lv.company.odata.api.ODataResult;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.search.TotalHitCountCollector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static lv.company.edup.infrastructure.lucene.impl.searcher.LuceneQueryBuilder.get;

public abstract class AbstractSearcher<T> implements Searcher<T> {

    @Override
    public ODataResult<T> search(ODataCriteria criteria) {
        IndexSearcher searcher = getIndexSearcher();

        try {
            Query searchQuery = buildSearchQuery(criteria);
            Sort sort = buildSort(criteria);

            Collection<Document> documents = find(criteria.getTop(), criteria.getSkip(), sort, searchQuery, searcher);
            List<Long> ids = getDocumentIds(documents);

            List<T> values = load(ids);

            ODataResult<T> result = new ODataResult<T>();
            result.setValues(sortResult(ids, values));

            if (criteria.isCount()) {
                result.setCount(count(searchQuery, searcher));
            }

            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ODataResult<T>();
    }

    @Override
    public Long count(Query query) {
        return count(query, getIndexSearcher());
    }

    @Override
    public Long count(Query query, IndexSearcher searcher) {
        try {
            TotalHitCountCollector collector = new TotalHitCountCollector();
            searcher.search(query, collector);
            return (long) collector.getTotalHits();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    private List<T> sortResult(List<Long> ids, List<T> values) {
        if (CollectionUtils.isEmpty(ids) || CollectionUtils.isEmpty(values)) {
            return Collections.emptyList();
        }

        Map<Long, T> map = AppCollectionUtils.map(values, new AppCollectionUtils.KeyTransformer<T, Long>() {
            @Override
            public Long transform(T t) {
                return getId(t);
            }
        });

        List<T> result = new ArrayList<T>();
        for (Long id : ids) {
            CollectionUtils.addIgnoreNull(result, map.get(id));
        }

        return result;
    }

    private List<Long> getDocumentIds(Collection<Document> documents) {
        List<Long> ids = new ArrayList<Long>();
        for (Document document : documents) {
            String value = document.getField(LuceneDocumentUtils.TECHNICAL_ID).stringValue();
            Long id = StringUtils.isNumeric(value) ? Long.valueOf(value) : null;
            CollectionUtils.addIgnoreNull(ids, id);
        }
        return ids;
    }

    private Query buildSearchQuery(ODataCriteria criteria) {
        return get()
                .search(criteria.getSearch())
                .fullTextSearchAttributes(getAttributes())
                .customFieldBoosts(getCustomBoosts())
                .searchParameters(getQueryParser().analyze(criteria.getFilter()))
                .build(getAnalyzer());
    }

    private Sort buildSort(ODataCriteria criteria) {
        return LuceneSorterQueryBuilder.get()
                .orderBy(getQueryParser().analyzeOrderBy(criteria.getOrderBy()))
                .build();
    }

    private Collection<Document> find(Integer top, Integer skip, Sort sort, Query query, IndexSearcher searcher) throws IOException {
        Collection<Document> documents = new ArrayList<Document>();
        TopFieldDocs docs = searcher.search(query, skip + top, sort);
        ScoreDoc[] scoreDocs = docs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            CollectionUtils.addIgnoreNull(documents, searcher.doc(scoreDoc.doc));
        }
        return documents;
    }


}
