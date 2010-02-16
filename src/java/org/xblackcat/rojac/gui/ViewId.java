package org.xblackcat.rojac.gui;

import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.MessageData;

/**
 * @author xBlackCat
 */

class ViewId {
    private final Integer forumId;
    private final Integer messageId;

    ViewId(Forum f) {
        this(f.getForumId(), null);
    }

    public ViewId(Integer forumId, Integer messageId) {
        this.forumId = forumId;
        this.messageId = messageId;
    }

    public ViewId(MessageData mes) {
        this(mes.getForumId(), mes.getMessageId());
    }

    public Integer getForumId() {
        return forumId;
    }

    public Integer getMessageId() {
        return messageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ViewId)) return false;

        ViewId viewId = (ViewId) o;

        return forumId == null ? viewId.forumId == null : forumId.equals(viewId.forumId) &&
                messageId == null ? viewId.messageId == null : messageId.equals(viewId.messageId);

    }

    @Override
    public int hashCode() {
        int result = forumId != null ? forumId.hashCode() : 0;
        result = 31 * result + (messageId != null ? messageId.hashCode() : 0);
        return result;
    }
}
