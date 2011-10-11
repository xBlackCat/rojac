package org.xblackcat.rojac.util;

import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.progress.ProgressChangeEvent;

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

        int size = e.getValue();
        String factors = " KMGTE";
        int idx = -1;

        int lastSize;
        do {
            lastSize = size;
            size >>= 10;
            idx++;
        } while (size > 10);

        return Message.ProgressControl_AffectedBytes.get(lastSize, factors.charAt(idx));
    }

}
