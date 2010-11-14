package org.xblackcat.rojac.data;

/**
 * @author xBlackCat
 */

public class MessageData {
    protected final int messageId;
    protected final int topicId;
    protected final int parentId;
    protected final int forumId;
    protected final int userId;
    protected final String subject;
    protected final String userName;
    protected final long messageDate;
    protected final long updateDate;
    protected final boolean read;
    protected final RatingCache rating;

    public MessageData(int messageId, int topicId, int parentId, int forumId, int userId, String subject, String userName, long messageDate, long updateDate, boolean read, String rating) {
        this.messageId = messageId;
        this.topicId = topicId;
        this.parentId = parentId;
        this.forumId = forumId;
        this.userId = userId;
        this.subject = subject;
        this.userName = userName;
        this.messageDate = messageDate;
        this.updateDate = updateDate;
        this.read = read;
        this.rating = new RatingCache(rating);
    }

    public int getMessageId() {
        return messageId;
    }

    public int getForumId() {
        return forumId;
    }

    public int getUserId() {
        return userId;
    }

    public String getSubject() {
        return subject;
    }

    public String getUserName() {
        return userName;
    }

    public long getMessageDate() {
        return messageDate;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public int getTopicId() {
        return topicId;
    }

    public int getParentId() {
        return parentId;
    }

    public boolean isRead() {
        return read;
    }

    public RatingCache getRating() {
        return rating;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("MessageData");
        sb.append("{messageId=").append(messageId);
        sb.append(", topicId=").append(topicId);
        sb.append(", parentId=").append(parentId);
        sb.append(", forumId=").append(forumId);
        sb.append(", userId=").append(userId);
        sb.append(", subject='").append(subject).append('\'');
        sb.append(", userName='").append(userName).append('\'');
        sb.append(", messageDate=").append(messageDate);
        sb.append(", updateDate=").append(updateDate);
        sb.append('}');
        return sb.toString();
    }
}
