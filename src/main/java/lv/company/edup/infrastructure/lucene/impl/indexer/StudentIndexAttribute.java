package lv.company.edup.infrastructure.lucene.impl.indexer;

import lv.company.edup.infrastructure.lucene.api.indexer.IndexAttribute;
import org.apache.commons.lang3.StringUtils;

public enum StudentIndexAttribute implements IndexAttribute {


    ID("Id"),
    NAME("Name"),
    LAST_NAME("LastName"),
    MAIL("Mail"),
    BIRTH_DATE("BirthDate"),
    MOBILE("Mobile"),
    PERSON_ID("PersonId");

    private final String value;

    StudentIndexAttribute(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
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
