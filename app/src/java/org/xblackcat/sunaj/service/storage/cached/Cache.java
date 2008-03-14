package org.xblackcat.sunaj.service.storage.cached;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Date: 15.04.2007
 *
 * @author ASUS
 */

final class Cache<T> implements IPurgable {
    private final Map<Object, SoftReference<T>> cache = new HashMap<Object, SoftReference<T>>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

    private final Lock rl = lock.readLock();
    private final Lock wl = lock.writeLock();

    Cache() {
    }

    public void purge() {
        wl.lock();
        try {
            cache.clear();
        } finally {
            wl.unlock();
        }
    }

    public T get(Object id) {
        rl.lock();
        T o = null;
        try {
            SoftReference<T> ref = cache.get(id);
            if (ref != null) {
                o = ref.get();
                // TODO: remove record if weak reference is invalidated 
            }
        } finally {
            rl.unlock();
        }
        return o;
    }

    public void put(Object id, T o) {
        wl.lock();
        try {
            cache.put(id, new SoftReference<T>(o));
        } finally {
            wl.unlock();
        }
    }

    public void remove(Object id) {
        wl.lock();
        try {
            cache.remove(id);
        } finally {
            wl.unlock();
        }
    }
}
