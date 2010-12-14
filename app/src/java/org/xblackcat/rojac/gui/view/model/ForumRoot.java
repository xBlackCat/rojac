package org.xblackcat.rojac.gui.view.model;

import org.xblackcat.rojac.data.MessageData;

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

    /**
     * Adds a thread to forum root.
     * @param newThread
     * @return <code>true</code> if thread was added and <code>false</code> if thread was updated.
     */
    final boolean addThread(Thread newThread) {
        if (childrenPosts.contains(newThread)) {
            int index = childrenPosts.indexOf(newThread);
            Thread realThread = (Thread) childrenPosts.get(index);
            if (!realThread.isFilled()) {
                childrenPosts.set(index, newThread);
                resort();
            }
            return false;
        } else {
            childrenPosts.add(newThread);
            resort();
            return true;
        }
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
