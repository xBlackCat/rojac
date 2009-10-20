package org.xblackcat.rojac.service.storage.cached;

import java.lang.ref.SoftReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author ASUS
 */

final class ObjectCache<T> implements IPurgable {
    private SoftReference<T> cache;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

    private final Lock rl = lock.readLock();
    private final Lock wl = lock.writeLock();

    ObjectCache() {
    }

    public void purge() {
        wl.lock();
        try {
            cache = null;
        } finally {
            wl.unlock();
        }
    }

    public T get() {
        rl.lock();
        T o = null;
        try {
            if (cache != null) {
                o = cache.get();
            }
        } finally {
            rl.unlock();
        }
        return o;
    }

    public void put(T o) {
        wl.lock();
        try {
            cache = new SoftReference<T>(o);
        } finally {
            wl.unlock();
        }
    }
}