package org.xblackcat.rojac.service.janus.commands;

/**
 * @author xBlackCat
 */

public class AffectedMessage {
    public static final AffectedMessage[] EMPTY = new AffectedMessage[0];
    public static final int DEFAULT_FORUM = 0;
    
    private final Integer messageId;
    private final int forumId;

    public AffectedMessage(int forumId) {
        this.forumId = forumId;
        messageId = null;
    }

    public AffectedMessage(int forumId, int messageId) {
        this.forumId = forumId;
        this.messageId = messageId;
    }

    public int getForumId() {
        return forumId;
    }

    public Integer getMessageId() {
        return messageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AffectedMessage)) return false;

        AffectedMessage that = (AffectedMessage) o;

        if (forumId != that.forumId) return false;
        if (messageId != null ? !messageId.equals(that.messageId) : that.messageId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = messageId != null ? messageId.hashCode() : 0;
        result = 31 * result + forumId;
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("AffectedMessage");
        sb.append("{forumId=").append(forumId);
        sb.append(", messageId=").append(messageId);
        sb.append('}');
        return sb.toString();
    }
}
