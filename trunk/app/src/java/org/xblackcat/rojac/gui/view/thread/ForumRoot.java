package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.data.MessageData;

/**
 * @author xBlackCat
 */

public class ForumRoot extends Post {
    public ForumRoot(int forumId) {
        super(constructDummyMessageItem(forumId), null);
    }

    @Override
    protected Post getThreadRoot() {
        throw new UnsupportedOperationException("There is no thread root for forum");
    }

    private static MessageData constructDummyMessageItem(int forumId) {
        return new MessageData(
                -1,
                -1,
                -1,
                forumId,
                -1,
                null,
                null,
                -1,
                -1
        );
    }
}
