package org.xblackcat.utils;

import java.util.Iterator;

/**
* 09.04.12 11:40
*
* @author xBlackCat
*/
public class ReadOnlyIterator<T> implements Iterator<T> {
    private final Iterator<T> iterator;

    public ReadOnlyIterator(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {
        return iterator.next();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Can't remove from read-only iterator");
    }
}
