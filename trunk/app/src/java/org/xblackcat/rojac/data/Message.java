package org.xblackcat.rojac.data;

import ru.rsdn.Janus.JanusMessageInfo;

import java.util.Date;

/**
 * Date: 14.04.2007
 *
 * @author ASUS
 */

public final class Message {
    private final int messageId;
    private final int topicId;
    private final int parentId;
    private final int userId;
    private final int forumId;
    private final String subject;
    private final String messageName;
    private final String userNick;
    private final String message;
    private final int articleId;
    private final long messageDate;
    private final long updateDate;
    private final Role userRole;
    private final String userTitle;
    private final int userTitleColor;
    private final long lastModerated;
    // Rojac-related values
    private final boolean notifyOnResponse;
    private final boolean read;
    private final Integer favoriteIndex;
    private final long resentChildDate;

    public Message(int articleId, int forumId, long lastModerated, String message, long messageDate, int messageId,
                   String messageName, int parentId, String subject, int topicId, long updateDate, int userId,
                   String userNick, Role userRole, String userTitle, int userTitleColor,
                   boolean notifyOnResponse, boolean read, Integer favoriteIndex, long resentChildDate) {
        this.read = read;
        this.articleId = articleId;
        this.forumId = forumId;
        this.lastModerated = lastModerated;
        this.message = message;
        this.messageDate = messageDate;
        this.messageId = messageId;
        this.messageName = messageName;
        this.parentId = parentId;
        this.subject = subject;
        this.topicId = topicId;
        this.updateDate = updateDate;
        this.userId = userId;
        this.userNick = userNick;
        this.userRole = userRole;
        this.userTitle = userTitle;
        this.userTitleColor = userTitleColor;
        this.notifyOnResponse = notifyOnResponse;
        this.favoriteIndex = favoriteIndex;
        this.resentChildDate = resentChildDate;
    }

    public Message(JanusMessageInfo i) {
        this(i.getArticleId(), i.getForumId(), i.getLastModerated().getTimeInMillis(), i.getMessage(),
                i.getMessageDate().getTimeInMillis(), i.getMessageId(), i.getMessageName(),
                i.getParentId(), i.getSubject(), i.getTopicId(), i.getUpdateDate().getTimeInMillis(),
                i.getUserId(), i.getUserNick(), Role.getUserType(i.getUserRole()), i.getUserTitle(),
                i.getUserTitleColor(), false, false, null, i.getMessageDate().getTimeInMillis());
    }

    public int getArticleId() {
        return articleId;
    }

    public int getForumId() {
        return forumId;
    }

    public long getLastModerated() {
        return lastModerated;
    }

    public String getMessage() {
        return message;
    }

    public long getMessageDate() {
        return messageDate;
    }

    public int getMessageId() {
        return messageId;
    }

    public String getMessageName() {
        return messageName;
    }

    public int getParentId() {
        return parentId;
    }

    public String getSubject() {
        return subject;
    }

    public int getTopicId() {
        return topicId;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserNick() {
        return userNick;
    }

    public Role getUserRole() {
        return userRole;
    }

    public String getUserTitle() {
        return userTitle;
    }

    public int getUserTitleColor() {
        return userTitleColor;
    }

    /**
     * Returns <code>true</code> if it is necessary to send notification on response on this message.
     *
     * @return
     */
    public boolean isNotifyOnResponse() {
        return notifyOnResponse;
    }

    /**
     * Returns the readness status of the message.
     * @return
     */
    public boolean isRead() {
        return read;
    }

    public Integer getFavoriteIndex() {
        return favoriteIndex;
    }

    public long getResentChildDate() {
        return resentChildDate;
    }

    public String toString() {
        StringBuilder str = new StringBuilder("Message[");
        str.append("messageId=").append(messageId).append(", ");
        str.append("topicId=").append(topicId).append(", ");
        str.append("parentId=").append(parentId).append(", ");
        str.append("userId=").append(userId).append(", ");
        str.append("forumId=").append(forumId).append(", ");
        str.append("userNick=").append(userNick).append(", ");
        str.append("articleId=").append(articleId).append(", ");
        str.append("messageDate=").append(messageDate).append(", ");
        str.append("updateDate=").append(updateDate).append(", ");
        str.append("userRole=").append(userRole).append(", ");
        str.append("userTitle=").append(userTitle).append(", ");
        str.append("userTitleColor=").append(userTitleColor).append(", ");
        str.append("notifyOnResponse=").append(notifyOnResponse).append(", ");
        str.append("read=").append(read).append(", ");
        str.append("favoriteIndex=").append(favoriteIndex).append(", ");
        str.append("messageName=").append(messageName).append(", ");
        str.append("subject=").append(subject).append(", ");
        str.append("message=").append(message).append(", ");
        str.append("lastModerated=").append(new Date(lastModerated)).append(']');
        return str.toString();
    }
}