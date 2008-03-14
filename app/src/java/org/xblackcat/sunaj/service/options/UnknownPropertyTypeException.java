package org.xblackcat.sunaj.service.options;

/**
 * Date: 13 квіт 2007
 *
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
