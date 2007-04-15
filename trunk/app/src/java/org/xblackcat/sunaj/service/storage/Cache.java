package org.xblackcat.sunaj.service.storage;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Date: 15.04.2007
 *
 * @author ASUS
 */

final class Cache<T> {
    private final Map<Integer, WeakReference<T>> cache = new HashMap<Integer, WeakReference<T>>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

    private final Lock rl = lock.readLock();
    private final Lock wl = lock.writeLock();

    Cache() {
    }

    public void purgeCache() {
        wl.lock();
        try {
            cache.clear();
        } finally {
            wl.unlock();
        }
    }

    public T getObject(int id) {
        rl.lock();
        T o = null;
        try {
            WeakReference<T> ref = cache.get(id);
            if (ref != null) {
                o = ref.get();
                // TODO: remove record if weak reference is invalidated 
            }
        } finally {
            rl.unlock();
        }
        return o;
    }

    public void putObject(int id, T o) {
        wl.lock();
        try {
            cache.put(id, new WeakReference<T>(o));
        } finally {
            wl.unlock();
        }
    }

    public void removeObject(int id) {
        wl.lock();
        try {
            cache.remove(id);
        } finally {
            wl.unlock();
        }
    }
}
