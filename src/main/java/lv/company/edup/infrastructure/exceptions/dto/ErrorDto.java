package lv.company.edup.infrastructure.exceptions.dto;

import lv.company.edup.infrastructure.response.ErrorCode;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorDto {

    private String path;
    private ErrorCode code;
    private String message;
    private String cause;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ErrorCode getCode() {
        return code;
    }

    public void setCode(ErrorCode code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }
}
