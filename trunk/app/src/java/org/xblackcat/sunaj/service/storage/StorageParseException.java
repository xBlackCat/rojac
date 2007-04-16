package org.xblackcat.sunaj.service.storage;

/**
 * Date: 16.04.2007
 *
 * @author ASUS
 */

public class StorageParseException extends StorageException {
    public StorageParseException() {
        super();
    }

    public StorageParseException(String message) {
        super(message);
    }

    public StorageParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageParseException(Throwable cause) {
        super(cause);
    }
}
