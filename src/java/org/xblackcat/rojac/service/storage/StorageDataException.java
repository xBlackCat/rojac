package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.RojacException;

/**
 * @author ASUS
 */

public class StorageDataException extends RojacException {
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
