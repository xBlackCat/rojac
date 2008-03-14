package org.xblackcat.sunaj.service.storage;

/**
 * Date: 16.04.2007
 *
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
