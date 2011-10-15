package org.xblackcat.rojac.gui.view.model;

import org.xblackcat.rojac.data.MessageData;

/**
 * @author xBlackCat
 */

public class ForumRoot extends Post {
    public ForumRoot(int forumId) {
        super(constructDummyMessageItem(forumId), null);
    }

    @Override
    public Thread getThreadRoot() {
        throw new UnsupportedOperationException("There is no thread root for forum");
    }

    @Override
    public boolean isIgnored() {
        return false;
    }

    /**
     * Searches through all threads for the message Id
     *
     * @param messageId
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

    public int removeThread(int threadId) {
        int i = 0;
        int childrenPostsSize = childrenPosts.size();

        while (i < childrenPostsSize) {
            Post p = childrenPosts.get(i);
            if (p.getMessageId() == threadId) {
                childrenPosts.remove(i);
                return i;
            }
            i++;
        }
        return -1;
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
                true,
                null,
                false,
                0,
                false);
    }
}
