package org.xblackcat.rojac.service.storage;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Helper class to simplify code to work with storage. Allowed to change storage in runtime.
 *
 * @author xBlackCat
 */
public class Storage {
    private static IStorage storage;

    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private final static Lock readLock = lock.readLock();
    private final static Lock writeLock = lock.writeLock();

    public static <T extends AH> T get(Class<T> ahClass) {
        readLock.lock();
        try {
            return storage.get(ahClass);
        } finally {
            readLock.unlock();
        }
    }

    public static void setStorage(IStorage newStorage) throws StorageException {
        if (newStorage == null) {
            throw new NullPointerException("Storage can not be null");
        }

        IStorage oldStorage = storage;

        writeLock.lock();
        try {
            newStorage.initialize();
            storage = newStorage;
        } finally {
            writeLock.unlock();
        }

        if (oldStorage != null) {
            oldStorage.shutdown();
        }
    }

    public static void shutdown() throws StorageException {
        storage.shutdown();
    }
}
