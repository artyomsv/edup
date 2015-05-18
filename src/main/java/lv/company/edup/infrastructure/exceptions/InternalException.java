package lv.company.edup.infrastructure.exceptions;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class InternalException extends EdupRuntimeException {

    public InternalException() {
    }

    public InternalException(String message) {
        super(message);
    }

    public InternalException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalException(Throwable cause) {
        super(cause);
    }

    public InternalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
