package org.xblackcat.rojac.service.options;

/**
 * @author Alexey
 */

public class UnknownPropertyTypeException extends RuntimeException {
    public UnknownPropertyTypeException() {
    }

    public UnknownPropertyTypeException(String message) {
        super(message);
    }

    public UnknownPropertyTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownPropertyTypeException(Throwable cause) {
        super(cause);
    }
}
