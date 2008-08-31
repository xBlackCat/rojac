package org.xblackcat.rojac.service.storage;

/**
 * Date: 15 трав 2007
 *
 * @author ASUS
 */

public interface IMiscAH extends AH {
    void storeExtraMessage(int messageId) throws StorageException;

    void removeExtraMessage(int messageId) throws StorageException;

    void clearExtraMessages() throws StorageException;

    int[] getExtraMessages() throws StorageException;
}
