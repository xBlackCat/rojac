package org.xblackcat.rojac.service.storage;

/**
 * @author ASUS
 */

public class StorageCheckException extends StorageException {
    public StorageCheckException() {
    }

    public StorageCheckException(String message) {
        super(message);
    }

    public StorageCheckException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageCheckException(Throwable cause) {
        super(cause);
    }
}
