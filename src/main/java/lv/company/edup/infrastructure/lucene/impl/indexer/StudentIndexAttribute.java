package lv.company.edup.infrastructure.lucene.impl.indexer;

import lv.company.edup.infrastructure.lucene.api.indexer.IndexAttribute;
import org.apache.commons.lang3.StringUtils;

public enum StudentIndexAttribute implements IndexAttribute {


    ID("Id", ""),
    NAME("Name", ""),
    LAST_NAME("LastName", ""),
    MAIL("Mail", ""),
    BIRTH_DATE("BirthDate", "Format"),
    CREATED("Created", "Format"),
    UPDATED("Updated", "Format"),
    MOBILE("Mobile", ""),
    PERSON_ID("PersonId", ""),
    PARENTS_INFO("ParentInfo", ""),
    CHARACTERISTICS("Characteristics", "");

    private final String value;
    private final String postFix;

    StudentIndexAttribute(String value, String postFix) {
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
            for (StudentIndexAttribute attribute : StudentIndexAttribute.values()) {
                if (StringUtils.equalsIgnoreCase(attribute.name(), value)) {
                    return attribute;
                }
            }
        }
        return null;
    }
}
