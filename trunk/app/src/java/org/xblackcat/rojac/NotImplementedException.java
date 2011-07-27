package org.xblackcat.rojac;

/**
 * @author xBlackCat Date: 27.07.11
 */
public class NotImplementedException extends RojacDebugException {
    public NotImplementedException() {
        super();
    }

    public NotImplementedException(Throwable cause) {
        super(cause);
    }

    public NotImplementedException(String message) {
        super(message);
    }

    public NotImplementedException(String message, Throwable cause) {
        super(message, cause);
    }
}
