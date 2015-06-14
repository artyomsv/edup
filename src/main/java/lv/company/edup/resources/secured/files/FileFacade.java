package lv.company.edup.resources.secured.files;

import lv.company.edup.infrastructure.exceptions.InternalException;
import lv.company.edup.infrastructure.mapping.ObjectMapper;
import lv.company.edup.infrastructure.response.ErrorResponseProvider;
import lv.company.edup.persistence.files.FileEntity;
import lv.company.edup.resources.ApplicationFacade;
import lv.company.edup.services.files.EdupFileItemFactory;
import lv.company.edup.services.files.FileService;
import lv.company.edup.services.files.dto.FileDto;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.fileupload.FileItem;

import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Singleton
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class FileFacade extends ApplicationFacade {

    @Inject ErrorResponseProvider provider;
    @Inject EdupFileItemFactory factory;
    @Inject FileService service;
    @Inject ObjectMapper mapper;

    public Response uploadFile(HttpServletRequest request) {
        List<FileItem> items = factory.parse(request);
        if (CollectionUtils.isEmpty(items)) {
            return notFound();
        }

        FileItem item = items.iterator().next();

        try {
            FileDto dto = service.persistFile(item);
            return ok(dto);
        } catch (IOException e) {
            throw new InternalException(e);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Response downloadFile(Long id) {
        FileEntity file = service.getFile(id);
        return streamResponse(file.getData(), file.getContentType().getContentType(), file.getName());
    }


    public Response getFilesInformation() {
        return ok(service.search());
    }

    public Response getFileInformation(Long id) {
        FileEntity file = service.getFile(id);
        return ok(mapper.map(file, FileDto.class));
    }
}
