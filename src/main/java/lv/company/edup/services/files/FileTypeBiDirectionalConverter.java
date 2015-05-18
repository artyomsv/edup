package lv.company.edup.services.files;

import lv.company.edup.persistence.files.FileType;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

public class FileTypeBiDirectionalConverter extends BidirectionalConverter<FileType, String> {
    @Override
    public String convertTo(FileType fileType, Type<String> type) {
        return fileType != null ? fileType.getContentType() : null;
    }

    @Override
    public FileType convertFrom(String s, Type<FileType> type) {
        return FileType.resolve(s);
    }
}
