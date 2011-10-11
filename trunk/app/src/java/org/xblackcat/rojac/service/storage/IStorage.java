package org.xblackcat.rojac.service.storage;

/**
 * @author ASUS
 */

public interface IStorage {

    <T extends AH> T get(Class<T> base);

    void shutdown() throws StorageException;
}
