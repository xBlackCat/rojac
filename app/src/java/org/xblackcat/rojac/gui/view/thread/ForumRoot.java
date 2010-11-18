package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.data.MessageData;

import java.util.Collection;

/**
 * @author xBlackCat
 */

public class ForumRoot extends Post {
    public ForumRoot(int forumId) {
        super(constructDummyMessageItem(forumId), null);
        read = true;
    }

    @Override
    public Thread getThreadRoot() {
        throw new UnsupportedOperationException("There is no thread root for forum");
    }

    final void addThread(Collection<Thread> threads) {
        for (Thread newThread : threads) {
            if (childrenPosts.contains(newThread)) {
                int index = childrenPosts.indexOf(newThread);
                Thread realThread = (Thread) childrenPosts.get(index);
                if (!realThread.isFilled()) {
                    childrenPosts.set(index, newThread);
                }
            } else {
                childrenPosts.add(newThread);
            }
        }
        resort();
    }

    /**
     * Searches through all threads for the message Id
     *
     * @param messageId
     *
     * @return
     */
    @Override
    public Post getMessageById(int messageId) {
        for (Post p : childrenPosts) {
            if (p.getMessageId() == messageId) {
                return p;
            } else {
                Post post = p.getMessageById(messageId);
                if (post != null) {
                    return post;
                }
            }
        }

        return null;
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
                false,
                null);
    }

    /**
     * Sets whole forum as read or as unread.
     *
     * @param read
     */
    @Override
    public void setRead(boolean read) {
        for (Post threadRoot : childrenPosts) {
            threadRoot.setDeepRead(read);
        }
    }
}
