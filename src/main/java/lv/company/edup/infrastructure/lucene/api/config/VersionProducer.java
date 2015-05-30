package lv.company.edup.infrastructure.lucene.api.config;

import org.apache.lucene.util.Version;

import javax.enterprise.inject.Produces;

public class VersionProducer {

    @Produces
    public Version getVersion() {
        return Version.LUCENE_5_1_0;
    }

}
