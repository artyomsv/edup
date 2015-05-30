package lv.company.edup.infrastructure.lucene.api.indexer;

public interface IndexAttribute {

    String getValue();

    IndexAttribute resolve(String value);

}
