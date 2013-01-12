package org.xblackcat.rojac.data;

/**
 * @author xBlackCat
 */

public class DiscussionStatistic extends ReadStatistic {
    public static final DiscussionStatistic NO_STAT = new DiscussionStatistic(0, 0, 0, null);
    private final Long lastMessageDate;

    public DiscussionStatistic(int totalMessages, int unreadMessages, int unreadReplies, Long lastMessageDate) {
        super(totalMessages, unreadMessages, unreadReplies);
        this.lastMessageDate = lastMessageDate;
    }

    public Long getLastMessageDate() {
        return lastMessageDate;
    }

    public DiscussionStatistic add(DiscussionStatistic s) {
        final Long date;
        if (lastMessageDate == null) {
            date = s.lastMessageDate;
        } else if (s.lastMessageDate == null) {
            date = lastMessageDate;
        } else {
            date = Math.max(lastMessageDate, s.lastMessageDate);
        }

        return new DiscussionStatistic(totalMessages + s.totalMessages, unreadMessages + s.unreadMessages, unreadReplies + s.unreadReplies, date);
    }
}
