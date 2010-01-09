package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.data.MessageData;

import java.util.Collection;

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

    final void addThreads(Collection<Thread> threads) {
        childrenPosts.addAll(threads);
        resort();
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

    /**
     * @param data
     * @return newly created item
     */
    public void insertPost(MessageData data) {
        int topicId = data.getTopicId();
        int messageId = data.getMessageId();

        if (topicId == 0) {
            // The message is a new topic
            Thread newThread = new Thread(data, this);

            insertChild(newThread);
        } else {
            for (Post p : childrenPosts) {
                if (p.getMessageId() == topicId) {
                    Thread thread = (Thread) p;

                    thread.insertChild(data);
                    return;
                }
            }
        }
    }
}
