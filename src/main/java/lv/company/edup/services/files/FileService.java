package lv.company.edup.services.files;

import lv.company.edup.infrastructure.exceptions.NotFoundException;
import lv.company.edup.infrastructure.mapping.ObjectMapper;
import lv.company.edup.persistence.files.FileEntity;
import lv.company.edup.persistence.files.FileRepository;
import lv.company.edup.persistence.files.FileType;
import lv.company.edup.services.files.dto.FileDto;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

@Singleton
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class FileService {

    @Inject FileRepository repository;
    @Inject ObjectMapper mapper;

    public FileDto persistFile(FileItem item) throws IOException {

        byte[] data = IOUtils.toByteArray(item.getInputStream());

        Checksum checksum = new CRC32();
        checksum.update(data, 0, data.length);
        Long value = checksum.getValue();

        List<FileEntity> list = repository.findByCheckSum(value);
        if (CollectionUtils.isNotEmpty(list)) {
            for (FileEntity entity : list) {
                if (entity.getSize() == data.length) {
                    return mapper.map(entity, FileDto.class);
                }
            }
        }

        FileEntity entity = new FileEntity();
        entity.setName(FilenameUtils.getName(item.getName()));
        entity.setContentType(FileType.resolve(FilenameUtils.getExtension(item.getName())));
        entity.setDate(new Date());
        entity.setSize(item.getSize());
        entity.setData(data);
        entity.setCheckSum(value);

        repository.persist(entity);

        return mapper.map(entity, FileDto.class);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public FileEntity getFile(Long id) {
        FileEntity file = repository.find(id);
        if (file == null) {
            throw new NotFoundException();
        }
        return file;
    }

    public Collection<FileDto> findFiles() {
        List<FileEntity> files = repository.findAll();
        return mapper.map(files, FileDto.class);
    }
}