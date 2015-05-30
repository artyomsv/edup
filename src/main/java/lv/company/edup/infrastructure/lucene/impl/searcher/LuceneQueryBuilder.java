package lv.company.edup.infrastructure.lucene.impl.searcher;

import lv.company.edup.infrastructure.exceptions.InternalException;
import lv.company.edup.infrastructure.lucene.api.indexer.IndexAttribute;
import lv.company.edup.infrastructure.lucene.impl.LuceneDocumentUtils;
import lv.company.odata.impl.parse.Relation;
import lv.company.odata.impl.parse.SearchParameter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static lv.company.edup.infrastructure.lucene.impl.LuceneDocumentUtils.extractTerms;
import static lv.company.edup.infrastructure.lucene.impl.LuceneDocumentUtils.getFullMatchField;
import static lv.company.edup.infrastructure.lucene.impl.LuceneDocumentUtils.getFullMatchValue;
import static lv.company.edup.infrastructure.lucene.impl.LuceneDocumentUtils.getLikeField;
import static lv.company.edup.infrastructure.lucene.impl.LuceneDocumentUtils.getLikeValue;
import static lv.company.edup.infrastructure.lucene.impl.LuceneDocumentUtils.sanitize;
import static lv.company.odata.api.SearchOperator.Type;
import static org.apache.lucene.queryparser.classic.QueryParser.Operator;
import static org.apache.lucene.search.BooleanClause.Occur;

public class LuceneQueryBuilder {

    private String search;
    private boolean fullTextSearch = false;
    private List<SearchParameter> searchParameters = Collections.emptyList();
    private Collection<? extends IndexAttribute> fullTextSearchAttributes = Collections.emptyList();
    private Map<IndexAttribute, Float> customFieldBoosts = Collections.emptyMap();

    public static LuceneQueryBuilder get() {
        return new LuceneQueryBuilder();
    }

    private LuceneQueryBuilder() {
    }

    public LuceneQueryBuilder search(String search) {
        if (StringUtils.isEmpty(search)) {
            return this;
        }

        search = StringUtils.trim(search);
        if (StringUtils.startsWith(search, "\"") && StringUtils.endsWith(search, "\"") && StringUtils.length(search) > 1) {
            this.search = search;
        } else {
            String[] split = StringUtils.split(search, "[ @\\+]");
            this.search = composeQuery(Type.LIKE, Relation.AND.name(), Arrays.asList(split));
        }

        this.fullTextSearch = !"*".equals(StringUtils.trim(this.search));
        return this;
    }

    public LuceneQueryBuilder searchParameters(List<SearchParameter> parameters) {
        if (CollectionUtils.isNotEmpty(parameters)) {
            this.searchParameters = parameters;
        }
        return this;
    }

    public LuceneQueryBuilder fullTextSearchAttributes(Collection<? extends IndexAttribute> attributes) {
        if (CollectionUtils.isNotEmpty(attributes)) {
            this.fullTextSearchAttributes = attributes;
        }
        return this;
    }

    public LuceneQueryBuilder customFieldBoosts(Map<IndexAttribute, Float> customFieldBoosts) {
        if (MapUtils.isNotEmpty(customFieldBoosts)) {
            this.customFieldBoosts = customFieldBoosts;
        }
        return this;
    }

    public Query build(Analyzer analyzer) {

        BooleanQuery query = new BooleanQuery();

        if (fullTextSearch) {
            if (CollectionUtils.isNotEmpty(fullTextSearchAttributes)) {
                BooleanQuery fullTextSearchQuery = new BooleanQuery();
                addFullTextSearchAttributes(fullTextSearchQuery, analyzer);
                addCustomFieldBoosts(fullTextSearchQuery, analyzer);
                query.add(fullTextSearchQuery, Occur.SHOULD);
            }
            query.add(getQuery(LuceneDocumentUtils.ALL, search, analyzer), Occur.SHOULD);
        }

        if (CollectionUtils.isNotEmpty(searchParameters)) {
            Relation relation = searchParameters.iterator().next().getRelation();
            if (fullTextSearch) {
                BooleanQuery parametersQuery = new BooleanQuery();
                addSearchParameter(parametersQuery, searchParameters, relation, analyzer);
                query.add(parametersQuery, Occur.MUST);
            } else {
                addSearchParameter(query, searchParameters, relation, analyzer);
            }
        }


        return query;
    }

    private void addSearchParameter(BooleanQuery query, Collection<SearchParameter> parameters, Relation relation, Analyzer analyzer) {
        for (SearchParameter parameter : parameters) {
            if (CollectionUtils.isNotEmpty(parameter.getChildren())) {
                for (SearchParameter children : parameter.getChildren()) {
                    if (CollectionUtils.isEmpty(children.getChildren())) {
                        addQuery(query, children, relation, analyzer);
                    } else {
                        BooleanQuery childQuery = new BooleanQuery();
                        addSearchParameter(childQuery, children.getChildren(), children.getRelation(), analyzer);
                        query.add(childQuery, getOccur(parameter.getRelation(), true));
                    }
                }
            }

            if (CollectionUtils.isNotEmpty(parameter.getValues())) {
                addQuery(query, parameter, relation, analyzer);
            }
        }
    }

    private void addQuery(BooleanQuery query, SearchParameter children, Relation relation, Analyzer analyzer) {
        String attribute = children.getName();
        String parameterQuery = composeQuery(children.getOperator(), Relation.OR.name(), children.getValues());
        boolean shouldOccur = !children.getOperator().isNegative();
        if (Type.EQUAL == children.getOperator() || Type.NOT_EQUAL == children.getOperator()) {
            attribute = getFullMatchField(attribute);
        } else if (Type.LIKE == children.getOperator()) {
            attribute = getLikeField(attribute);
        }

        query.add(getQuery(attribute, parameterQuery, analyzer), getOccur(relation, shouldOccur));
    }

    private Occur getOccur(Relation relation, boolean shouldOccur) {
        if (relation == Relation.OR) {
            return Occur.SHOULD;
        } else {
            return shouldOccur ? Occur.MUST : Occur.MUST_NOT;
        }
    }

    private void addFullTextSearchAttributes(BooleanQuery query, Analyzer analyzer) {
        for (IndexAttribute attribute : fullTextSearchAttributes) {

            String fullMatchField = getFullMatchField(attribute.getValue());

            for (String term : extractTerms(search)) {
                Query termFullMatchQuery = getQuery(fullMatchField, "\"" + getFullMatchValue(term) + "\"", analyzer);
                termFullMatchQuery.setBoost(10f);
                query.add(termFullMatchQuery, Occur.SHOULD);
            }
            String searchValue = "\"" + getFullMatchValue(search.replace("*", "").replace(" AND ", " ")) + "\"";

            Query fullTextMatchQuery = getQuery(fullMatchField, searchValue, analyzer);
            fullTextMatchQuery.setBoost(3f);
            query.add(fullTextMatchQuery, Occur.SHOULD);
        }
    }

    private void addCustomFieldBoosts(BooleanQuery query, Analyzer analyzer) {
        for (Map.Entry<IndexAttribute, Float> entry : customFieldBoosts.entrySet()) {
            Query attributeQuery = getQuery(entry.getKey().getValue(), search, analyzer);
            attributeQuery.setBoost(entry.getValue());
            query.add(attributeQuery, Occur.SHOULD);
        }
    }

    private Query getQuery(String attribute, String searchText, Analyzer analyzer) {
        QueryParser queryParser = getQueryParser(attribute, analyzer);
        queryParser.setAllowLeadingWildcard(true);
        queryParser.setDefaultOperator(Operator.AND);

        try {
            return queryParser.parse(searchText);
        } catch (ParseException e) {
            throw new InternalException(e);
        }
    }

    private String composeQuery(Type type, String combineWith, Collection<String> values) {
        StrBuilder query = new StrBuilder();
        if (CollectionUtils.isNotEmpty(values)) {
            for (String value : values) {
                value = StringUtils.isNotEmpty(value) ? sanitize(value).replace("%", "*") : value;
                if (query.length() > 0) {
                    query.append(" ").append(combineWith).append(" ");
                }
                query.append(wrapValue(value, type));
            }
        }
        return query.toString();
    }

    private String wrapValue(String value, Type type) {
        switch (type) {
            case LIKE:
                return "*" + getLikeValue(value) + "*";
            case EQUAL:
            case NOT_EQUAL:
                return "\"" + getFullMatchValue(value) + "\"";
            case GREATER_EQUAL_THAN:
                return "[" + value + " TO *]";
            case LESS_EQUAL_THAN:
                return "[* TO" + value + "]";
            case EQUAL_CASE_INSENSITIVE:
            default:
                return value;

        }
    }

    private QueryParser getQueryParser(String attribute, Analyzer analyzer) {
        return new QueryParser(attribute, analyzer);
    }

}
