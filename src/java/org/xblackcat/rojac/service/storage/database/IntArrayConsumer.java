package org.xblackcat.rojac.service.storage.database;

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import org.xblackcat.sjpu.storage.ConsumeException;
import org.xblackcat.sjpu.storage.ann.MapRowTo;
import org.xblackcat.sjpu.storage.consumer.IRowSetConsumer;

/**
 * 22.04.2014 18:44
 *
 * @author xBlackCat
 */
@MapRowTo(Integer.class)
public class IntArrayConsumer implements IRowSetConsumer<int[], Integer> {
    private final TIntList list = new TIntArrayList();

    @Override
    public int[] getRowsHolder() {
        return list.toArray();
    }

    @Override
    public boolean consume(Integer o) throws ConsumeException {
        if (o == null) {
            throw new ConsumeException("Can't consume null value");
        }

        list.add(o);
        return false;
    }
}
