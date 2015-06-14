package lv.company.edup.infrastructure.pdf.impl;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import lv.company.edup.infrastructure.pdf.api.ReportGenerationService;
import org.apache.commons.io.IOUtils;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.enterprise.context.ApplicationScoped;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

@ApplicationScoped
public class PDFDocumentsGenerationServiceImpl implements ReportGenerationService {

    private Logger logger = Logger.getLogger(PDFDocumentsGenerationServiceImpl.class.getSimpleName());

    @Override
    public byte[] generate(String html) throws DocumentException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            ITextRenderer renderer = new ITextRenderer();
            SharedContext context = renderer.getSharedContext();
//            context.setReplacedElementFactory(new Base64ImgRe);
            renderer.setDocumentFromString(html);
            renderer.getFontResolver().addFont("/Users/Artyom/Desktop/Java/Projects/edup/src/main/resources/fonts/arialuni.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            renderer.layout();
            renderer.createPDF(baos);
            return baos.toByteArray();
        } finally {
            IOUtils.closeQuietly(baos);
        }
    }

    private Path resolve(String file) {
        Path path = Paths.get(file);
        if (!path.toFile().exists()) {
            File root = new File("");
            File resources = new File(root.getAbsolutePath(), "");
            path = Paths.get(resources.getAbsolutePath(), file);
        }
        return path;
    }

}
