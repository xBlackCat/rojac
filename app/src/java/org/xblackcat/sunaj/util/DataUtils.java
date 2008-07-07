package org.xblackcat.sunaj.util;

import org.xblackcat.sunaj.data.IRSDNable;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

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

    public static String construstDebugSQL(String sql, Object... params) {
        String query = sql;

        for (Object o : params) {
            String str;
            if (o instanceof String) {
                str = "'" + Matcher.quoteReplacement(o.toString()) + "'";
            } else {
                str = Matcher.quoteReplacement(o.toString());
            }
            query = query.replaceFirst("\\?", str);
        }

        return query;
    }

    /*
    * Util methods for converting values.
    */
    public static <T extends Enum<T>> T convertToEnum(Class<T> enumClass, String val) {
        if (val != null) {
            return Enum.valueOf(enumClass, val);
        } else {
            return null;
        }
    }
}
