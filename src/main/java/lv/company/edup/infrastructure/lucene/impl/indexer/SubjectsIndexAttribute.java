package lv.company.edup.infrastructure.lucene.impl.indexer;

import lv.company.edup.infrastructure.lucene.api.indexer.IndexAttribute;
import org.apache.commons.lang3.StringUtils;

public enum SubjectsIndexAttribute implements IndexAttribute {

    ID("Id", ""),
    NAME("Name", ""),
    CREATED("Created", "Format");

    private final String value;
    private final String postFix;

    SubjectsIndexAttribute(String value, String postFix) {
        this.value = value;
        this.postFix = postFix;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getPostFix() {
        return postFix;
    }


    @Override
    public IndexAttribute resolve(String value) {
        if (StringUtils.isNotEmpty(value)) {
            for (SubjectsIndexAttribute attribute : SubjectsIndexAttribute.values()) {
                if (StringUtils.equalsIgnoreCase(attribute.name(), value)) {
                    return attribute;
                }
            }
        }
        return null;
    }
}
