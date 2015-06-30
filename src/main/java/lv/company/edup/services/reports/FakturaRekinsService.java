package lv.company.edup.services.reports;

import lv.company.edup.infrastructure.pdf.api.ReportGenerationService;
import lv.company.edup.infrastructure.response.UriUtils;
import lv.company.edup.infrastructure.templates.api.TemplateEngine;
import lv.company.edup.infrastructure.templates.api.TemplateName;
import lv.company.edup.infrastructure.templates.api.VelocityEngine;
import lv.company.edup.infrastructure.templates.impl.TemplateCache;
import lv.company.edup.infrastructure.templates.impl.templates.FakturaContextCreator;
import lv.company.edup.infrastructure.templates.impl.templates.dto.FakturaData;
import lv.company.edup.infrastructure.templates.impl.templates.dto.PageData;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Map;

@ApplicationScoped
public class FakturaRekinsService {

    @Inject TemplateCache cache;
    @Inject FakturaContextCreator fakturaContextCreator;
    @Inject @VelocityEngine TemplateEngine engine;
    @Inject ReportGenerationService service;
    @Inject UriUtils utils;

    public String prepareFakturaRekins() {
        FakturaData data = new FakturaData();
        PageData page = new PageData();
        page.setPageTitle("Faktura rekins");
        page.setHost(utils.getRootUrl());
        data.setPage(page);
        Map<String, Object> context = fakturaContextCreator.create(data);
        return engine.render(cache.getTemplate(TemplateName.Faktura), context);
    }
}
