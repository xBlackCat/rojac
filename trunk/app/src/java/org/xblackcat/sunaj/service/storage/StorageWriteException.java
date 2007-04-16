package org.xblackcat.sunaj.service.storage;

/**
 * Date: 16.04.2007
 *
 * @author ASUS
 */

public class StorageWriteException extends StorageException {
    public StorageWriteException() {
        super();
    }

    public StorageWriteException(String message) {
        super(message);
    }

    public StorageWriteException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageWriteException(Throwable cause) {
        super(cause);
    }
}