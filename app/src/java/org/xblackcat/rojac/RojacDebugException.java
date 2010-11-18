package org.xblackcat.rojac;

/**
 * Debug exception to handle non-conditional states of the application.
 *
 * @author xBlackCat
 */

public class RojacDebugException extends RuntimeException {
    public RojacDebugException() {
    }

    public RojacDebugException(String message) {
        super(message);
    }

    public RojacDebugException(String message, Throwable cause) {
        super(message, cause);
    }

    public RojacDebugException(Throwable cause) {
        super(cause);
    }
}
