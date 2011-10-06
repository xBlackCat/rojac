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
    protected final int parentUserId;

    /**
     * A new instance of MessageData object. The object contain significant data to show item in tree view.
     *
     * @param messageId    message id. By the id message can be accessed either in Rojac or on rsdn.ru site.
     * @param topicId      topic id. If zero - the message is topic starter.
     * @param parentId     previous message by hierarhy.
     * @param forumId      forum id of the message.
     * @param userId       internal user id
     * @param subject      message subject line
     * @param userName     the message author name
     * @param messageDate  create date of the message
     * @param updateDate   date the message being updated
     * @param read         flag: is the message has been read
     * @param rating       rating cache string of the message
     * @param ignored      boolean flag is teh message(topic) was ignored
     * @param parentUserId author of previous message by hierarhy. Zero for anonymous or topic head messages.
     */
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
                       boolean ignored,
                       int parentUserId) {
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
                ignored,
                parentUserId);
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
                        boolean ignored, int parentUserId) {
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
        this.parentUserId = parentUserId;
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

    public boolean isIgnored() {
        return ignored;
    }

    public int getParentUserId() {
        return parentUserId;
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
        sb.append(", read=").append(read);
        sb.append(", rating=").append(rating);
        sb.append(", ignored=").append(ignored);
        sb.append(", parentUserId=").append(parentUserId);
        sb.append('}');
        return sb.toString();
    }

    /**
     * Create a new MessageData (if necessary) with new state of read flag.
     *
     * @param read new read state
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
                ignored,
                parentUserId);
    }

    public MessageData setIgnored(boolean ignored) {
        if (ignored == this.ignored) {
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
                read,
                rating,
                ignored, // Not a field
                parentUserId);

    }
}
