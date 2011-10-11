package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.util.RojacUtils;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Helper class to simplify code to work with storage. Allowed to change storage in runtime.
 *
 * @author xBlackCat
 */
public final class Storage {
    private static IStorage storage;

    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public static <T extends AH> T get(Class<T> ahClass) {
        lock.readLock().lock();
        try {
            return storage.get(ahClass);
        } finally {
            lock.readLock().unlock();
        }
    }

    static void setStorage(IStorage newStorage) throws StorageException {
        assert RojacUtils.checkThread(false);

        IStorage oldStorage = storage;

        lock.writeLock().lock();
        try {
            storage = newStorage;
        } finally {
            lock.writeLock().unlock();
        }

        if (oldStorage != null) {
            oldStorage.shutdown();
        }
    }

    public static void shutdown() throws StorageException {
        lock.writeLock().lock();
        try {
            if (storage != null) {
                storage.shutdown();
            }
        } finally {
            lock.writeLock().unlock();
        }

    }
}
