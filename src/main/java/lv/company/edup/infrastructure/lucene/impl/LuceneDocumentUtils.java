package lv.company.edup.infrastructure.lucene.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.lucene.queryparser.flexible.standard.QueryParserUtil;

import java.util.Arrays;
import java.util.Collection;

public class LuceneDocumentUtils {

    public static final String TECHNICAL_ID = "TechnicalId";
    public static final String ALL = "all";
    public static final String RELEVANCE = "Relevance";

    public static final int MAX_CHAR = 0xDB99;
    public static final int LOW_PRIORITY_BORDERLINE = 0xC000;
    public static final String LOWEST_PRIORITY_STRING = String.valueOf((char) MAX_CHAR) + String.valueOf((char) MAX_CHAR);

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
        if (StringUtils.isBlank(value)) {
            return LOWEST_PRIORITY_STRING;
        }

        return lowerPriorityForNonLetters(StringUtils.lowerCase(value));
    }

    public static String lowerPriorityForNonLetters(String input) {
        int length = StringUtils.length(input);

        StrBuilder builder = new StrBuilder(length);
        for (int i = 0; i < length; i++) {
            char charToCheck = input.charAt(i);
            if (Character.isLetter(charToCheck)) {
                builder.append(charToCheck);
            } else {
                int proposeCode = (int) charToCheck + LOW_PRIORITY_BORDERLINE;
                if (proposeCode <= MAX_CHAR) {
                    builder.append((char) proposeCode);
                } else {
                    builder.append(charToCheck);
                }
            }
        }

        return builder.toString();
    }

    public static Collection<String> extractTerms(String query) {
        return Arrays.asList(sanitize(query.replace(" AND ", " ")).split("\\s+"));
    }

}
