package org.xblackcat.rojac.gui.view;

import org.xblackcat.rojac.gui.ViewId;

/**
 * @author xBlackCat
 */

public class ViewIds {
    public static ViewId getForumId(int forumId) {
        return new ItemId(forumId, ViewType.Forum);
    }

    public static ViewId getMessageId(int messageId) {
        return new ItemId(messageId, ViewType.SingleMessage);
    }

}
