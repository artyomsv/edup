package lv.company.edup.infrastructure.exceptions;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class NotFoundException extends EdupRuntimeException {

    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }
}
