package org.xblackcat.sunaj.gui.frame.mtree;

/**
 * Date: 17 груд 2007
 *
 * @author xBlackCat
 */

public final class Utils {
    private Utils() {
    }

    public static int getTopicShift(MessageItem item) {
        return item.getHierarchyLevel() << 2;
    }
}
