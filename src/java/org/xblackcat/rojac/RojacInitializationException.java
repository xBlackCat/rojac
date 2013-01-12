package org.xblackcat.rojac;

/**
 * 12.09.11 10:16
 *
 * @author xBlackCat
 */
public class RojacInitializationException extends RojacException {
    public RojacInitializationException() {
        super();
    }

    public RojacInitializationException(String message) {
        super(message);
    }

    public RojacInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RojacInitializationException(Throwable cause) {
        super(cause);
    }
}
