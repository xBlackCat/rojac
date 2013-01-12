package org.xblackcat.rojac.service.storage.database.helper;

import org.xblackcat.rojac.service.storage.IResult;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.utils.ReadOnlyIterator;

import java.util.Iterator;
import java.util.List;

/**
 * 09.04.12 11:39
 *
 * @author xBlackCat
 */
class PreloadedResult<T> implements IResult<T> {
    private final Iterable<T> res;

    public PreloadedResult(List<T> res) {
        this.res = res;
    }

    @Override
    public void close() throws StorageException {
        // Nothing to close :)
    }

    @Override
    public Iterator<T> iterator() {
        return new ReadOnlyIterator<>(res.iterator());
    }

}
