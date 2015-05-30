package lv.company.edup.infrastructure.lucene.api.searcher;

import lv.company.edup.infrastructure.lucene.api.config.IndexType;
import lv.company.edup.infrastructure.lucene.api.indexer.IndexAttribute;
import lv.company.odata.api.ODataCriteria;
import lv.company.odata.api.ODataResult;
import lv.company.odata.impl.parse.QueryParser;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public interface Searcher<T> {

    ODataResult<T> search(ODataCriteria criteria);

    Long count(Query query);

    Long count(Query query, IndexSearcher searcher);

    List<T> load(Collection<Long> ids);

    IndexType getType();

    IndexSearcher getIndexSearcher();

    Logger getLogger();

    Analyzer getAnalyzer();

    Collection<? extends IndexAttribute> getAttributes();

    Map<IndexAttribute, Float> getCustomBoosts();

    QueryParser getQueryParser();

    Long getId(T t);
}
