package lv.company.edup.infrastructure.lucene.impl;

import lv.company.edup.infrastructure.lucene.api.indexer.IndexAttribute;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.SortedNumericDocValuesField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.util.BytesRef;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static lv.company.edup.infrastructure.lucene.impl.LuceneDocumentUtils.getFullMatchField;
import static lv.company.edup.infrastructure.lucene.impl.LuceneDocumentUtils.getFullMatchValue;
import static lv.company.edup.infrastructure.lucene.impl.LuceneDocumentUtils.getLikeField;
import static lv.company.edup.infrastructure.lucene.impl.LuceneDocumentUtils.getLikeValue;
import static lv.company.edup.infrastructure.lucene.impl.LuceneDocumentUtils.getRangeField;
import static lv.company.edup.infrastructure.lucene.impl.LuceneDocumentUtils.getSortValue;
import static lv.company.edup.infrastructure.lucene.impl.LuceneDocumentUtils.getSortableField;
import static org.apache.lucene.document.Field.Store;

public class LuceneDocumentBuilder {

    private Document document;
    private Collection<Field> docFields = new ArrayList<Field>();

    public static LuceneDocumentBuilder get() {
        return new LuceneDocumentBuilder();
    }

    private LuceneDocumentBuilder() {
        document = new Document();
    }

    public LuceneDocumentBuilder add(IndexAttribute field, Date value) {
        return add(field, value, true);
    }

    public LuceneDocumentBuilder add(IndexAttribute field, Date value, boolean create) {
        if (field != null && value != null) {
            long time = value.getTime();

            docFields.add(new SortedNumericDocValuesField(getSortableField(field.getValue()), time));
            if (create) {
                document.add(new SortedNumericDocValuesField(getSortableField(field.getValue()), time));
            }

            docFields.add(new NumericDocValuesField(getRangeField(field.getValue()), time));
            if (create) {
                document.add(new NumericDocValuesField(getRangeField(field.getValue()), time));
            }

            add(field.getValue() + field.getPostFix(), DateFormatUtils.format(value, "yyyyMMdd"), false, create);
        }
        return this;
    }

    public LuceneDocumentBuilder add(IndexAttribute field, String value) {
        return add(field, value, true);
    }

    public LuceneDocumentBuilder add(IndexAttribute field, String value, boolean create) {
        if (field != null && value != null) {
            add(field.getValue(), String.valueOf(value), true, create);
        }
        return this;
    }

    public LuceneDocumentBuilder add(IndexAttribute field, Number value) {
        return add(field, value, true);
    }

    public LuceneDocumentBuilder add(IndexAttribute field, Number value, boolean create) {
        if (field != null && value != null) {
            add(field.getValue(), String.valueOf(value), false);

            docFields.add(new SortedNumericDocValuesField(getSortableField(field.getValue()), value.longValue()));
            if (create) {
                document.add(new SortedNumericDocValuesField(getSortableField(field.getValue()), value.longValue()));
            }

            docFields.add(new NumericDocValuesField(getRangeField(field.getValue()), value.longValue()));
            if (create) {
                document.add(new NumericDocValuesField(getRangeField(field.getValue()), value.longValue()));
            }
        }
        return this;
    }

    private LuceneDocumentBuilder add(String field, String value, boolean sortable) {
        return add(field, value, sortable, true);
    }

    private LuceneDocumentBuilder add(String field, String value, boolean sortable, boolean create) {
        if (field == null || StringUtils.isEmpty(value)) {
            return this;
        }

        value = StringUtils.trim(value);
        document.add(new TextField(field, value, Store.YES));
        document.add(new TextField(getFullMatchField(field), getFullMatchValue(value), Store.NO));
        document.add(new TextField(getLikeField(field), getLikeValue(value), Store.NO));
        if (sortable) {
            docFields.add(new SortedDocValuesField(getSortableField(field), new BytesRef(getSortValue(value))));
            if (create) {
                document.add(new SortedDocValuesField(getSortableField(field), new BytesRef(getSortValue(value))));
            }
        }
        return this;
    }

    public LuceneDocumentBuilder addFullTextSearch(Collection<? extends IndexAttribute> attributes) {
        return addFullTextSearch(attributes, true);
    }

    public LuceneDocumentBuilder addFullTextSearch(Collection<? extends IndexAttribute> attributes, boolean create) {
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

        if (!create) {
            for (Field field : docFields) {
                builder.append(" ").append(field.stringValue());
            }
        }

        document.add(new TextField(LuceneDocumentUtils.ALL, LuceneDocumentUtils.sanitize(StringUtils.trim(builder.toString())), Store.YES));
        return this;
    }


    public Document build() {
        return document;
    }

    public Collection<Field> getDocFields() {
        return docFields;
    }
}
