package org.xblackcat.rojac.data;

/**
 * Date: 21 вер 2008
 *
 * @author xBlackCat
 */

public class ForumStatistic {
    private int totalMessages;
    private int unreadMessages;

    public ForumStatistic(int totalMessages, int unreadMessages) {
        this.totalMessages = totalMessages;
        this.unreadMessages = unreadMessages;
    }

    public int getTotalMessages() {
        return totalMessages;
    }

    public void setTotalMessages(int totalMessages) {
        this.totalMessages = totalMessages;
    }

    public int getUnreadMessages() {
        return unreadMessages;
    }

    public void setUnreadMessages(int unreadMessages) {
        this.unreadMessages = unreadMessages;
    }
}
