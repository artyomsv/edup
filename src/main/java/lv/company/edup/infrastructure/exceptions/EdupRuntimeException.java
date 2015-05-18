package lv.company.edup.infrastructure.exceptions;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class EdupRuntimeException extends RuntimeException {

    public EdupRuntimeException() {
    }

    public EdupRuntimeException(String message) {
        super(message);
    }

    public EdupRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public EdupRuntimeException(Throwable cause) {
        super(cause);
    }

    public EdupRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
