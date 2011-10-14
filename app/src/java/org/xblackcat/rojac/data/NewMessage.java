package org.xblackcat.rojac.data;

import ru.rsdn.Janus.PostMessageInfo;

/**
 * @author ASUS
 */

public final class NewMessage implements IRSDNable<PostMessageInfo> {
    private final int localMessageId;
    private final int parentId;
    private final int forumId;
    private final String subject;
    private final String message;
    private final boolean draft;

    public NewMessage(int localMessageId, int parentId, int forumId, String subject, String message, boolean draft) {
        this.localMessageId = localMessageId;
        this.parentId = parentId;
        this.forumId = forumId;
        this.subject = subject;
        this.message = message;
        this.draft = draft;
    }

    public int getLocalMessageId() {
        return localMessageId;
    }

    public int getParentId() {
        return parentId;
    }

    public int getForumId() {
        return forumId;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public PostMessageInfo getRSDNObject() {
        assert !draft : "Got draft as message to send";

        return new PostMessageInfo(
                localMessageId,
                parentId,
                forumId,
                subject,
                message
        );
    }

    public String toString() {
        StringBuilder str = new StringBuilder("NewMessage[");
        str.append("localMessageId=").append(localMessageId).append(", ");
        str.append("parentId=").append(parentId).append(", ");
        str.append("forumId=").append(forumId).append(", ");
        str.append("subject=").append(subject).append(", ");
        str.append("message=").append(message).append(']');
        return str.toString();
    }

    public boolean isDraft() {
        return draft;
    }
}
