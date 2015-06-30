package lv.company.edup.reports;

import lv.company.edup.infrastructure.response.UriUtils;
import lv.company.edup.infrastructure.templates.impl.TemplateCache;
import lv.company.edup.infrastructure.templates.impl.VelocityPropertiesProducer;
import lv.company.edup.infrastructure.templates.impl.VelocityTemplateEngine;
import lv.company.edup.infrastructure.templates.impl.templates.FakturaContextCreator;
import lv.company.edup.services.reports.FakturaRekinsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FakturaRekinsTest {

    @Mock UriUtils utils;
    @Spy VelocityPropertiesProducer properties;
    @Spy VelocityTemplateEngine engine;
    @Spy TemplateCache cache;
    @Spy FakturaContextCreator fakturaContextCreator;
    @InjectMocks FakturaRekinsService service;

    @Before
    public void setUp() throws Exception {
        cache.init();
        engine.setProperties(properties.getVelocityProperties());
        engine.init();
        Mockito.when(utils.getRootUrl()).thenReturn("https://192.168.1.101:8443/edup/");
    }

    @Test
    public void testName() throws Exception {
        String fakturaRekins = service.prepareFakturaRekins();
        System.out.println(fakturaRekins);
    }
}
