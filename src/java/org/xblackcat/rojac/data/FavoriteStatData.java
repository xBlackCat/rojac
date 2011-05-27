package org.xblackcat.rojac.data;

import org.xblackcat.rojac.i18n.Messages;

/**
 * @author xBlackCat
 */

public class FavoriteStatData {
    private final int unread;
    private final int total;

    public FavoriteStatData(int unread, int total) {
        this.total = total;
        this.unread = unread;
    }

    public int getTotal() {
        return total;
    }

    public int getUnread() {
        return unread;
    }

    public String asString() {
        return Messages.View_Favorites_Statistic_Data.get(getUnread(), getTotal());
    }
}
