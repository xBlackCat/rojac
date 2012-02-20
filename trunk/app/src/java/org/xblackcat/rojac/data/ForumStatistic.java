package org.xblackcat.rojac.data;

/**
 * @author xBlackCat
 */

public class ForumStatistic extends ReadStatistic {
    private final int forumId;
    private final Long lastMessageDate;

    public ForumStatistic(int forumId, int totalMessages, int unreadMessages, Long lastMessageDate, int unreadReplies) {
        super(unreadReplies, unreadMessages, totalMessages);
        this.forumId = forumId;
        this.lastMessageDate = lastMessageDate;
    }

    public Long getLastMessageDate() {
        return lastMessageDate;
    }

    public int getForumId() {
        return forumId;
    }

    public static ForumStatistic noStatistic(int forumId) {
        return new ForumStatistic(forumId, 0, 0, null, 0);
    }
}
