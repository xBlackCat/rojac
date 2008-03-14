package org.xblackcat.sunaj.service.data;

import ru.rsdn.Janus.JanusModerateInfo;

import java.util.Date;

/**
 * Date: 14.04.2007
 *
 * @author ASUS
 */

public final class Moderate {
    private final int messageId;
    private final int userId;
    private final int forumId;
    private final long creationTime;

    public Moderate(int messageId, int userId, int forumId, long creationTime) {
        this.messageId = messageId;
        this.userId = userId;
        this.forumId = forumId;
        this.creationTime = creationTime;
    }

    public Moderate(JanusModerateInfo i) {
        this(i.getMessageId(), i.getUserId(), i.getForumId(), i.getCreate().getTimeInMillis());
    }

    public int getMessageId() {
        return messageId;
    }

    public int getUserId() {
        return userId;
    }

    public int getForumId() {
        return forumId;
    }

    public long getCreationTime() {
        return creationTime;
    }
        
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Moderate that = (Moderate) o;

        if (creationTime != that.creationTime) return false;
        if (forumId != that.forumId) return false;
        if (messageId != that.messageId) return false;
        if (userId != that.userId) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = messageId;
        result = 31 * result + userId;
        result = 31 * result + forumId;
        result = 31 * result + (int) (creationTime ^ (creationTime >>> 32));
        return result;
    }

    public String toString() {
        StringBuilder str = new StringBuilder("Moderate[");
        str.append("messageId=").append(messageId).append(", ");
        str.append("userId=").append(userId).append(", ");
        str.append("forumId=").append(forumId).append(", ");
        str.append("creationTime=").append(new Date(creationTime)).append(']');
        return str.toString();
    }
}
