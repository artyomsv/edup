package lv.company.edup.services.files;

import lv.company.edup.infrastructure.DataSize;
import lv.company.edup.infrastructure.exceptions.BadRequestException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

@ApplicationScoped
public class EdupFileItemFactory {

    @Inject ServletContext context;

    private DiskFileItemFactory factory;

    @PostConstruct
    public void init() {
        factory = new DiskFileItemFactory();
        File repository = (File) context.getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(repository);
        FileCleaningTracker tracker = FileCleanerCleanup.getFileCleaningTracker(context);
        factory.setFileCleaningTracker(tracker);
    }

    public List<FileItem> parse(HttpServletRequest request) {
        try {
            ServletFileUpload upload = new ServletFileUpload(factory);
            DataSize dataSize = new DataSize(2, DataSize.Unit.MEGABYTE);
            upload.setSizeMax(dataSize.toBytes());

            return upload.parseRequest(request);
        } catch (FileUploadException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

}
