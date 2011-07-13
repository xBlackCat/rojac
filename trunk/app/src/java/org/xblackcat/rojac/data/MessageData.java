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
    protected final boolean ignored;

    public MessageData(int messageId,
                       int topicId,
                       int parentId,
                       int forumId,
                       int userId,
                       String subject,
                       String userName,
                       long messageDate,
                       long updateDate,
                       boolean read,
                       String rating,
                       boolean ignored) {
        this(messageId,
                topicId,
                parentId,
                forumId,
                userId,
                subject,
                userName,
                messageDate,
                updateDate,
                read,
                new RatingCache(rating),
                ignored);
    }

    private MessageData(int messageId,
                        int topicId,
                        int parentId,
                        int forumId,
                        int userId,
                        String subject,
                        String userName,
                        long messageDate,
                        long updateDate,
                        boolean read,
                        RatingCache rating,
                        boolean ignored) {
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
        this.rating = rating;
        this.ignored = ignored;
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

    /**
     * Returns *real* data for the correspond field. Note that thread start posts have the field == 0.
     *
     * @return a topic head id or 0 if the message is a topic head.
     */
    public int getTopicId() {
        return topicId;
    }

    /**
     * Returns topic head message id: if {@linkplain #getTopicId()} == 0, the messageId will be returned.
     *
     * @return zero-safe version for obtaining topic start message id.
     */
    public int getThreadRootId() {
        return topicId == 0 ? messageId : topicId;
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

    /**
     * Create a new MessageData (if necessary) with new state of read flag.
     *
     * @param read new read state
     *
     * @return MessageData object with specified read state.
     */
    public MessageData setRead(boolean read) {
        if (read == this.read) {
            return this;
        }

        return new MessageData(
                messageId,
                topicId,
                parentId,
                forumId,
                userId,
                subject,
                userName,
                messageDate,
                updateDate,
                read, // Not a field
                rating,
                ignored
        );
    }
}
