package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.data.MessageData;

import java.util.Collection;
import java.util.Collections;

/**
 * @author xBlackCat
 */

public class ForumRoot extends Post {
    public ForumRoot(int forumId) {
        super(constructDummyMessageItem(forumId), null);
    }

    @Override
    protected Thread getThreadRoot() {
        throw new UnsupportedOperationException("There is no thread root for forum");
    }

    final void addThread(Collection<Thread> threads) {
        childrenPosts.addAll(threads);
        Collections.sort(childrenPosts);
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
                -1,
                false);
    }
}
