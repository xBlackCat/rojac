package org.xblackcat.sunaj.service.storage;

/**
 * Date: 15.04.2007
 *
 * @author ASUS
 */

public class StorageException extends Exception {
    public StorageException() {
    }

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageException(Throwable cause) {
        super(cause);
    }
}
