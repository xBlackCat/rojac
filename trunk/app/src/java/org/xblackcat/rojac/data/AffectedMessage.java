package org.xblackcat.rojac.data;

/**
 * @author xBlackCat
 */

public class AffectedMessage {
    private final int messageId;
    private final int topicId;
    private final int forumId;

    public AffectedMessage(int messageId, int topicId, int forumId) {
        this.messageId = messageId;
        this.topicId = topicId;
        this.forumId = forumId;
    }

    public int getMessageId() {
        return messageId;
    }

    public int getTopicId() {
        return topicId;
    }

    public int getForumId() {
        return forumId;
    }
}
