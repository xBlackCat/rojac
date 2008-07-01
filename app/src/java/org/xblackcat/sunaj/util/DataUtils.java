package org.xblackcat.sunaj.util;

import org.xblackcat.sunaj.data.IRSDNable;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 27 черв 2008
 *
 * @author xBlackCat
 */

public class DataUtils {
    private DataUtils() {
    }

    public static <T extends Serializable> T[] getRSDNObject(IRSDNable<T>[] ar) {
        Class<T> c = (Class<T>) ((ParameterizedType)ar.getClass().getComponentType().getGenericInterfaces()[0]).getActualTypeArguments()[0];

        List<T> res = new ArrayList<T>(ar.length);

        for (IRSDNable<T> o : ar) {
            res.add(o.getRSDNObject());
        }

        T[] a = (T[]) Array.newInstance(c, ar.length);
        return res.toArray(a);
    }
}
