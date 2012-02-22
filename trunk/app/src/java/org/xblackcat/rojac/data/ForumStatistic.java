package org.xblackcat.rojac.data;

/**
 * @author xBlackCat
 */

public class ForumStatistic extends ReadStatistic {
    public static final ForumStatistic NO_STAT = new ForumStatistic(0, 0, null, 0);
    private final Long lastMessageDate;

    public ForumStatistic(int totalMessages, int unreadMessages, Long lastMessageDate, int unreadReplies) {
        super(unreadReplies, unreadMessages, totalMessages);
        this.lastMessageDate = lastMessageDate;
    }

    public Long getLastMessageDate() {
        return lastMessageDate;
    }
}
