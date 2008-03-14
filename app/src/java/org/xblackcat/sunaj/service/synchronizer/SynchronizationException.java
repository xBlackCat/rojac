package org.xblackcat.sunaj.service.synchronizer;

/**
 * Date: 12 трав 2007
 *
 * @author ASUS
 */

public class SynchronizationException extends Exception {
    public SynchronizationException() {
    }

    public SynchronizationException(String message) {
        super(message);
    }

    public SynchronizationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SynchronizationException(Throwable cause) {
        super(cause);
    }
}
