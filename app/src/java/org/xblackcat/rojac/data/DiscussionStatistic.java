package org.xblackcat.rojac.data;

/**
 * @author xBlackCat
 */

public class DiscussionStatistic extends ReadStatistic {
    public static final DiscussionStatistic NO_STAT = new DiscussionStatistic(0, 0, null, 0);
    private final Long lastMessageDate;

    public DiscussionStatistic(int totalMessages, int unreadMessages, Long lastMessageDate, int unreadReplies) {
        super(unreadReplies, unreadMessages, totalMessages);
        this.lastMessageDate = lastMessageDate;
    }

    public Long getLastMessageDate() {
        return lastMessageDate;
    }
}
