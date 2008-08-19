package org.xblackcat.sunaj.service.storage;

import org.xblackcat.sunaj.SunajException;

/**
 * Date: 15.04.2007
 *
 * @author ASUS
 */

public class StorageException extends SunajException {
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
