package org.xblackcat.rojac.util;

import org.apache.commons.collections.CollectionUtils;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.progress.ProgressChangeEvent;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author xBlackCat
 */

public final class SynchronizationUtils {

    private SynchronizationUtils() {
    }

    public static String makeSizeString(ProgressChangeEvent e) {
        if (e.getValue() == null || e.getValue() < 0) {
            return "";
        }

        long size = e.getValue();
        String factors = " KMGTE";
        int idx = -1;

        long lastSize;
        do {
            lastSize = size;
            size >>= 10;
            idx++;
        } while (size > 10);

        return Message.ProgressControl_AffectedBytes.get(lastSize, factors.charAt(idx));
    }

    public static <T> Collection<T> collect(Iterable<T> it) {
        Collection<T> col = new ArrayList<>();
        CollectionUtils.addAll(col, it.iterator());
        return col;
    }
}
