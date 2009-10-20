package org.xblackcat.rojac.gui.frame.mtree;

/**
 * @author xBlackCat
 */

public final class Utils {
    private Utils() {
    }

    public static int getTopicShift(MessageItem item) {
        return item.getHierarchyLevel() << 2;
    }
}
