package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.RojacException;

/**
 * @author ASUS
 */

public class StorageException extends RojacException {
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
