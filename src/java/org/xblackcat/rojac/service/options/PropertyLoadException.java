package org.xblackcat.rojac.service.options;

/**
 * @author xBlackCat
 */

public class PropertyLoadException extends RuntimeException {
    public PropertyLoadException() {
    }

    public PropertyLoadException(String message) {
        super(message);
    }

    public PropertyLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public PropertyLoadException(Throwable cause) {
        super(cause);
    }
}
