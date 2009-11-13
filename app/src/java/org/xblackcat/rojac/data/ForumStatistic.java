package org.xblackcat.rojac.data;

/**
 * @author xBlackCat
 */

public class ForumStatistic {
    private int totalMessages;
    private int unreadMessages;
    private Long lastMessageDate;

    public ForumStatistic(int totalMessages, int unreadMessages, Long lastMessageDate) {
        this.totalMessages = totalMessages;
        this.unreadMessages = unreadMessages;
        this.lastMessageDate = lastMessageDate;
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

    public Long getLastMessageDate() {
        return lastMessageDate;
    }

    public void setLastMessageDate(Long lastMessageDate) {
        this.lastMessageDate = lastMessageDate;
    }
}
