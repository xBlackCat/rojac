package org.xblackcat.sunaj.service.storage;

/**
 * Date: 16.04.2007
 *
 * @author ASUS
 */

public class StorageDataException extends StorageException {
    public StorageDataException() {
        super();
    }

    public StorageDataException(String message) {
        super(message);
    }

    public StorageDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageDataException(Throwable cause) {
        super(cause);
    }
}
