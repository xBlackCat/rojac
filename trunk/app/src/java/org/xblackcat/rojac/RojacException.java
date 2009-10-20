package org.xblackcat.rojac;

/**
 * @author xBlackCat
 */

public class RojacException extends Exception {
    public RojacException() {
    }

    public RojacException(String message) {
        super(message);
    }

    public RojacException(String message, Throwable cause) {
        super(message, cause);
    }

    public RojacException(Throwable cause) {
        super(cause);
    }
}
