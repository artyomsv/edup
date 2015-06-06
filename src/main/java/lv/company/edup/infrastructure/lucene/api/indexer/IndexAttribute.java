package lv.company.edup.infrastructure.lucene.api.indexer;

public interface IndexAttribute {

    String getValue();

    String getPostFix();

    IndexAttribute resolve(String value);

}
