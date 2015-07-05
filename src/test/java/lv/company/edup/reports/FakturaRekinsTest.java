package lv.company.edup.reports;

import lv.company.edup.infrastructure.templates.api.ContextCreator;
import lv.company.edup.infrastructure.templates.api.TemplateEngine;
import lv.company.edup.infrastructure.templates.api.TemplateName;
import lv.company.edup.infrastructure.templates.api.Type;
import lv.company.edup.infrastructure.templates.impl.JasperTemplateEngine;
import lv.company.edup.infrastructure.templates.impl.TemplateCache;
import lv.company.edup.infrastructure.templates.impl.templates.FakturaContextCreator;
import lv.company.edup.infrastructure.templates.impl.templates.dto.FakturaData;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Map;

public class FakturaRekinsTest {

    ContextCreator<FakturaData> creator = new FakturaContextCreator();
    TemplateEngine engine = new JasperTemplateEngine();
    TemplateCache cache;

    @Before
    public void setUp() throws Exception {
        cache = new TemplateCache();
        cache.init();
        creator = new FakturaContextCreator();
        engine = new JasperTemplateEngine();
    }

    @After
    public void tearDown() throws Exception {
        cache = null;
        creator = null;
        engine = null;
    }

    @Test
    public void testName() throws Exception {
        FakturaData from = new FakturaData();
        from.setPaymentDescription("This is some description");
        from.setPaymentId(1L);
        from.setPaymentTotal(256L);
        Map<String, Object> context = creator.create(from);
        FileUtils.writeByteArrayToFile(new File("faktura.pdf"), engine.render(cache.getTemplate(TemplateName.FakturaJasper), context, Type.PDF));
//        FileUtils.writeByteArrayToFile(new File("faktura.html"), engine.render(cache.getTemplate(TemplateName.FakturaJasper), context, Type.HTML));
//        FileUtils.writeByteArrayToFile(new File("faktura.xlsX"), engine.render(cache.getTemplate(TemplateName.FakturaJasper), context, Type.XLSX));

    }
}
