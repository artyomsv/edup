package lv.company.edup.batch;

import org.easybatch.core.api.Engine;
import org.easybatch.core.api.RecordProcessingException;
import org.easybatch.core.api.RecordProcessor;
import org.easybatch.core.api.Report;
import org.easybatch.core.impl.EngineBuilder;
import org.easybatch.core.reader.StringRecordReader;
import org.easybatch.core.record.StringRecord;
import org.junit.Test;

public class TestEasyBatch {

    @Test
    public void testName() throws Exception {

        String dataSource = "1,foo,easy batch rocks! #EasyBatch\n 2,bar,@foo I do confirm :-)";

        Engine engine = new EngineBuilder()
                .named("Hello World Job")
                .reader(new StringRecordReader(dataSource))
                .processor(new RecordProcessor<StringRecord, StringRecord>() {
                    @Override
                    public StringRecord processRecord(StringRecord o) throws RecordProcessingException {
                        System.out.println(o.getPayload());
                        return o;
                    }
                })
                .build();

        Report call = engine.call();
        System.out.println(call);

    }
}
