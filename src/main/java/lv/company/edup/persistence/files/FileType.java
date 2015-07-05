package lv.company.edup.persistence.files;

import org.apache.commons.lang3.StringUtils;

public enum FileType {

    PDF("pdf", "application/pdf"),
    DOC("doc", "application/msword"),
    DOCX("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    PPT("ppt", "application/vnd.ms-powerpoint"),
    PPTX("ppt", "application/vnd.openxmlformats-officedocument.presentationml.presentation"),
    XLS("xls", "application/vnd.ms-excel"),
    XLSX("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    XML("xml", "application/xml"),
    JSON("json", "application/json"),
    JPG("jpg", "image/jpeg"),
    JPEG("jpeg", "image/jpeg"),
    PNG("png", "image/png"),
    TIFF("tiff", "image/tiff"),
    GIF("gif", "image/gif"),
    BMP("bmp", "image/bmp"),
    ICO("ico", "image/ico"),
    PSD("psd", "image/vnd.adobe.photoshop"),
    Z7("7z", "application/x-7z-compressed"),
    RAR("rar", "application/x-rar-compressed"),
    ZIP("zip", "application/zip"),
    EPUB("epub", "application/epub+zip"),


    UNKNOWN("unknown", "unknown");

    private final String extension;
    private final String contentType;

    FileType(String extension, String contentType) {
        this.extension = extension;
        this.contentType = contentType;
    }

    public String getExtension() {
        return extension;
    }

    public String getContentType() {
        return contentType;
    }

    public static FileType resolve(String value) {
        if (StringUtils.isNotEmpty(value)) {
            for (FileType type : FileType.values()) {
                if (StringUtils.equalsIgnoreCase(type.extension, value) || StringUtils.equalsIgnoreCase(type.contentType, value)) {
                    return type;
                }
            }

        }

        return UNKNOWN;
    }
}
