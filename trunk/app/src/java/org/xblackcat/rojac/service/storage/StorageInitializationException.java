package org.xblackcat.rojac.service.storage;

/**
 * @author ASUS
 */

public class StorageInitializationException extends StorageException {
    public StorageInitializationException() {
    }

    public StorageInitializationException(String message) {
        super(message);
    }

    public StorageInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageInitializationException(Throwable cause) {
        super(cause);
    }
}