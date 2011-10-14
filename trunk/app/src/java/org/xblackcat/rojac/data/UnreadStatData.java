package org.xblackcat.rojac.data;

import org.xblackcat.rojac.i18n.Message;

/**
 * @author xBlackCat
 */

public class UnreadStatData {
    private final int unread;
    private final int total;

    public UnreadStatData(int unread, int total) {
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
        return asString(Message.View_Favorites_Statistic_Data);
    }

    public String asString(Message statRenderString) {
        return unread == total || unread == 0 ? String.valueOf(total) : statRenderString.get(getUnread(), getTotal());
    }
}
