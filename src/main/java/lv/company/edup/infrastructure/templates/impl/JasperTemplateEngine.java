package lv.company.edup.infrastructure.templates.impl;

import lv.company.edup.infrastructure.templates.api.JasperEngine;
import lv.company.edup.infrastructure.templates.api.Template;
import lv.company.edup.infrastructure.templates.api.TemplateEngine;
import lv.company.edup.infrastructure.templates.api.Type;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.apache.commons.io.IOUtils;

import javax.enterprise.context.ApplicationScoped;
import java.io.ByteArrayOutputStream;
import java.util.Map;

@JasperEngine
@ApplicationScoped
public class JasperTemplateEngine implements TemplateEngine {

    @Override
    public byte[] render(Template template, Map<String, Object> context, Type type) {

        try {
            JRProperties.setProperty("net.sf.jasperreports.default.pdf.encoding","UTF-8");
            JRProperties.setProperty("net.sf.jasperreports.default.pdf.embedded","true");

            JRDataSource dataSource = new JREmptyDataSource();
            JasperReport compiledJasperReport = JasperCompileManager.compileReport(IOUtils.toInputStream(template.getTemplate()));
            JasperPrint jasperPrint = JasperFillManager.fillReport(compiledJasperReport, context, dataSource);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            SimpleExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
            switch (type) {
                case HTML:
                    HtmlExporter htmlExporter = new HtmlExporter();
                    htmlExporter.setExporterInput(exporterInput);
                    htmlExporter.setExporterOutput(new SimpleHtmlExporterOutput(outputStream));
                    htmlExporter.exportReport();
                    return outputStream.toByteArray();
                case PDF:
                    JRPdfExporter pdfExporter = new JRPdfExporter();
                    pdfExporter.setExporterInput(exporterInput);
                    pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
                    pdfExporter.exportReport();
                    return outputStream.toByteArray();
                case XLSX:
                    JRXlsxExporter exporter = new JRXlsxExporter();
                    exporter.setExporterInput(exporterInput);
                    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
                    exporter.exportReport();
                    return outputStream.toByteArray();
            }

        } catch (JRException e) {
            e.printStackTrace();
        }

        return null;
    }

}