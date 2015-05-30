package lv.company.edup.infrastructure.lucene.impl;

import lv.company.edup.infrastructure.lucene.api.indexer.IndexAttribute;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.Builder;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexableField;

import java.util.Collection;
import java.util.Date;

import static lv.company.edup.infrastructure.lucene.impl.LuceneDocumentUtils.ALL;
import static lv.company.edup.infrastructure.lucene.impl.LuceneDocumentUtils.getFullMatchField;
import static lv.company.edup.infrastructure.lucene.impl.LuceneDocumentUtils.getFullMatchValue;
import static lv.company.edup.infrastructure.lucene.impl.LuceneDocumentUtils.getLikeField;
import static lv.company.edup.infrastructure.lucene.impl.LuceneDocumentUtils.getLikeValue;
import static lv.company.edup.infrastructure.lucene.impl.LuceneDocumentUtils.getRangeField;
import static lv.company.edup.infrastructure.lucene.impl.LuceneDocumentUtils.getSortValue;
import static lv.company.edup.infrastructure.lucene.impl.LuceneDocumentUtils.getSortableField;
import static lv.company.edup.infrastructure.lucene.impl.LuceneDocumentUtils.sanitize;
import static org.apache.lucene.document.Field.Store;

public class LuceneDocumentBuilder implements Builder<Document> {

    private Document document;

    public static LuceneDocumentBuilder get() {
        return new LuceneDocumentBuilder();
    }

    private LuceneDocumentBuilder() {
        document = new Document();
    }

    public LuceneDocumentBuilder add(IndexAttribute field, Date value) {
        if (field != null && value != null) {
            add(field, value.getTime());
            add(field, DateFormatUtils.format(value, "yyyy/MM/dd"));
            add(field, DateFormatUtils.format(value, "yyyyMMdd"));
        }
        return this;
    }

    public LuceneDocumentBuilder add(IndexAttribute field, String value) {
        if (field != null && value != null) {
            add(field, String.valueOf(value), true);
        }
        return this;
    }

    public LuceneDocumentBuilder add(IndexAttribute field, Number value) {
        if (field != null && value != null) {
            add(field, String.valueOf(value), false);
            document.add(new LongField(getSortableField(field.getValue()), value.longValue(), Store.NO));
            document.add(new LongField(getRangeField(field.getValue()), value.longValue(), Store.YES));
        }
        return this;
    }

    public LuceneDocumentBuilder add(IndexAttribute field, String value, boolean sortable) {
        if (field == null || StringUtils.isEmpty(value)) {
            return this;
        }

        value = StringUtils.trim(value);
        document.add(new TextField(field.getValue(), value, Store.YES));
        document.add(new TextField(getFullMatchField(field.getValue()), getFullMatchValue(value), Store.NO));
        document.add(new TextField(getLikeField(field.getValue()), getLikeValue(value), Store.NO));
        if (sortable) {
            document.add(new StringField(getSortableField(field.getValue()), getSortValue(value), Store.NO));
        }
        return this;
    }

    public LuceneDocumentBuilder addFullTextSearch(Collection<? extends IndexAttribute> attributes) {
        if (CollectionUtils.isEmpty(attributes)) {
            return this;
        }

        StrBuilder builder = new StrBuilder();

        for (IndexAttribute attribute : attributes) {
            IndexableField[] fields = document.getFields(attribute.getValue());
            for (IndexableField field : fields) {
                builder.append(" ").append(field.stringValue());
            }
        }
        document.add(new TextField(ALL, sanitize(StringUtils.trim(builder.toString())), Store.YES));
        return this;
    }


    @Override
    public Document build() {
        return document;
    }
}
