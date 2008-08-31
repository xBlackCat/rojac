package org.xblackcat.rojac;

/**
 * Date: 8 лип 2008
 *
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
