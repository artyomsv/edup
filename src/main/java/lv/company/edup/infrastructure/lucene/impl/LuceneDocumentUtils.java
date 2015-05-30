package lv.company.edup.infrastructure.lucene.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.queryparser.flexible.standard.QueryParserUtil;

import java.util.Arrays;
import java.util.Collection;

public class LuceneDocumentUtils {

    public static final String TECHNICAL_ID = "TechnicalId";
    public static final String ALL = "all";
    public static final String RELEVANCE = "Relevance";

    public static String getSortableField(String field) {
        return field + "_sort";
    }

    public static String getRangeField(String field) {
        return field + "_range";
    }

    public static String getFullMatchField(String field) {
        return field + "_full";
    }

    public static String getLikeField(String field) {
        return field + "_like";
    }

    public static String getFullMatchValue(String value) {
        return "_start_" + sanitize(value) + "_finish_";
    }

    public static String getLikeValue(String value) {
        return sanitize(value).replace(" ", "");
    }

    public static String sanitize(String value) {
        return QueryParserUtil.escape(value.replace("*", "").replace(".", "").replace(",", ""));
    }

    public static String getSortValue(String value) {
        if (StringUtils.isEmpty(value) || value.trim().isEmpty()) {
            return String.valueOf(Character.MAX_VALUE) + String.valueOf(Character.MAX_VALUE);
        }

        value = StringUtils.lowerCase(value);
        value = StringUtils.replacePattern(value, "[\\p{Punct}]", "");
        value = StringUtils.trim(value);

        if (StringUtils.isEmpty(value)) {
            return String.valueOf(Character.MAX_VALUE) + String.valueOf(Character.MAX_VALUE);
        }

        if (value.matches("^[\\d\\p{Punct}].*")) {
            value = Character.MAX_VALUE + value;
        }
        return value;
    }

    public static Collection<String> extractTerms(String query) {
        return Arrays.asList(sanitize(query.replace(" AND ", " ")).split("\\s+"));
    }

}
