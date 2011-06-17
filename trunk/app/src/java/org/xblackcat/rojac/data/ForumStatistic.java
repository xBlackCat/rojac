package org.xblackcat.rojac.data;

/**
 * @author xBlackCat
 */

public class ForumStatistic {
    private final int forumId;
    private final int totalMessages;
    private final int unreadMessages;
    private final Long lastMessageDate;

    public ForumStatistic(int forumId, int totalMessages, int unreadMessages, Long lastMessageDate) {
        this.forumId = forumId;
        this.totalMessages = totalMessages;
        this.unreadMessages = unreadMessages < 0 ? 0 : unreadMessages > totalMessages ? totalMessages : unreadMessages;
        this.lastMessageDate = lastMessageDate;
    }

    public int getTotalMessages() {
        return totalMessages;
    }

    public int getUnreadMessages() {
        return unreadMessages;
    }

    public Long getLastMessageDate() {
        return lastMessageDate;
    }

    public int getForumId() {
        return forumId;
    }

    public ForumStatistic adjustUnread(int amount) {
        return  new ForumStatistic(
                this.forumId,
                this.totalMessages,
                this.unreadMessages + amount,
                this.lastMessageDate
        );
    }
}
