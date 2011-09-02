package org.xblackcat.rojac.data;

/**
 * @author xBlackCat
 */
public class ReadStatistic {
    protected final int totalMessages;
    protected final int unreadMessages;
    protected final int unreadReplies;

    public ReadStatistic(int unreadReplies, int unreadMessages, int totalMessages) {
        this.unreadReplies = unreadReplies;
        this.unreadMessages = unreadMessages < 0 ? 0 : unreadMessages > totalMessages ? totalMessages : unreadMessages;
        this.totalMessages = totalMessages;
    }

    public int getTotalMessages() {
        return totalMessages;
    }

    public int getUnreadMessages() {
        return unreadMessages;
    }

    public int getUnreadReplies() {
        return unreadReplies;
    }
}
