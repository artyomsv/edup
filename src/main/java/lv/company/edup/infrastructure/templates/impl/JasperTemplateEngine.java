package lv.company.edup.infrastructure.templates.impl;

import lv.company.edup.infrastructure.exceptions.InternalException;
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

import javax.enterprise.context.ApplicationScoped;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;

@JasperEngine
@ApplicationScoped
public class JasperTemplateEngine implements TemplateEngine {

    public static final String JR_DATA_SOURCE = "JRDataSource";

    @Override
    public void init() {

    }

    @Override
    public byte[] render(byte[] template, Map<String, Object> context, Type type) {

        try {
            JRProperties.setProperty("net.sf.jasperreports.default.pdf.encoding", "UTF-8");
            JRProperties.setProperty("net.sf.jasperreports.default.pdf.embedded", "true");

            Object dataSource = context.get(JR_DATA_SOURCE);

            JasperReport compiledJasperReport = JasperCompileManager.compileReport(new ByteArrayInputStream(template));
            JasperPrint jasperPrint;
            if (dataSource != null) {
                jasperPrint = JasperFillManager.fillReport(compiledJasperReport, context, (JRDataSource) dataSource);
            } else {
                jasperPrint = JasperFillManager.fillReport(compiledJasperReport, context, new JREmptyDataSource());

            }
//            JasperPrint jasperPrint = JasperFillManager.fillReport(new ByteArrayInputStream(template.getCompiledTemplate()), context, dataSource);

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
                default:
                    return null;
            }

        } catch (JRException e) {
            throw new InternalException(e);
        }
    }

    @Override
    public byte[] render(Template template, Map<String, Object> context, Type type) {
        return render(template.getTemplate(), context, type);

    }


}
