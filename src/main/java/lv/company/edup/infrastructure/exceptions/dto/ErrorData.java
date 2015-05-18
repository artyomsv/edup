package lv.company.edup.infrastructure.exceptions.dto;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;

@XmlRootElement
public class ErrorData {

    private Collection<ErrorDto> errors = new ArrayList<ErrorDto>();

    public Collection<ErrorDto> getErrors() {
        return errors;
    }

    public void setErrors(Collection<ErrorDto> errors) {
        this.errors = errors;
    }
}
