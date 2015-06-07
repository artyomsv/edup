package lv.company.edup.infrastructure.lucene.impl.searcher;

import lv.company.edup.infrastructure.lucene.impl.LuceneDocumentUtils;
import lv.company.odata.impl.parse.SortingDefinition;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortedNumericSortField;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static lv.company.edup.infrastructure.lucene.impl.LuceneDocumentUtils.RELEVANCE;
import static lv.company.edup.infrastructure.lucene.impl.LuceneDocumentUtils.TECHNICAL_ID;
import static lv.company.edup.infrastructure.lucene.impl.LuceneDocumentUtils.getSortableField;
import static org.apache.lucene.search.SortField.Type;

public class LuceneSorterQueryBuilder {

    private Collection<SortingDefinition> definitions = Collections.emptyList();
    private Collection<String> numericFields = Collections.emptyList();

    public static LuceneSorterQueryBuilder get() {
        return new LuceneSorterQueryBuilder();
    }

    private LuceneSorterQueryBuilder() {
    }

    public LuceneSorterQueryBuilder orderBy(Collection<SortingDefinition> definitions) {
        if (CollectionUtils.isNotEmpty(definitions)) {
            this.definitions = definitions;
        }
        return this;
    }

    public LuceneSorterQueryBuilder numericFields(Collection<String> numericFields) {
        if (CollectionUtils.isNotEmpty(numericFields)) {
            this.numericFields = numericFields;
        }

        return this;
    }

    public Sort build() {
        return buildSort(getSortFields());
    }

    private Collection<SortField> getSortFields() {
        Collection<SortField> sortFields = new ArrayList<SortField>();
        for (SortingDefinition definition : definitions) {
            String attribute = definition.getAttribute();
            if (attribute != null && definition.getDirection() != null) {
                CollectionUtils.addIgnoreNull(sortFields, buildSortFields(attribute, definition.getDirection().isAscending()));
            }
        }
        return sortFields;
    }

    private SortField buildSortFields(String field, boolean ascending) {
        Type type;
        if (CollectionUtils.isNotEmpty(numericFields) && numericFields.contains(field)) {
            type = Type.LONG;
        } else {
            type = Type.STRING;
        }

        if (StringUtils.equalsIgnoreCase(RELEVANCE, field)) {
            return new SortField(null, Type.SCORE, !ascending);
        } else {
            if (type == Type.LONG) {
                return new SortedNumericSortField(getSortableField(field), type, !ascending);
            } else {
                return new SortField(getSortableField(field), type, !ascending);
            }
        }
    }

    private Sort buildSort(Collection<SortField> sortFields) {
        if (CollectionUtils.isEmpty(sortFields)) {
            return new Sort(new SortField(LuceneDocumentUtils.getSortableField(TECHNICAL_ID), Type.STRING, false));
        } else {
            sortFields.add(SortField.FIELD_SCORE);
            return new Sort(sortFields.toArray(new SortField[sortFields.size()]));
        }
    }
}
