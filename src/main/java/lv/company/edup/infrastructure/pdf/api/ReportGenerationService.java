package lv.company.edup.infrastructure.pdf.api;

import com.lowagie.text.DocumentException;

import java.io.IOException;

public interface ReportGenerationService {

    byte[] generate(String html) throws DocumentException, IOException;
}
