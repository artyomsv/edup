package lv.company.edup.infrastructure.lucene.api.config;

import org.apache.lucene.analysis.Analyzer;

import javax.enterprise.inject.Produces;

public class AnalyzerProducer {

    @Produces
    @IndexAnalyzer
    public Analyzer getAnalyzer() {
        return new AppAnalyzer();
    }

}
