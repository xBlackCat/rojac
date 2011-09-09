package org.xblackcat.rojac.service.datahandler;

import gnu.trove.set.hash.TIntHashSet;

/**
 * @author xBlackCat
 */

public class SynchronizationCompletePacket extends APacket implements IForumUpdatePacket, IMessageUpdatePacket, IThreadsUpdatePacket {
    private final TIntHashSet forumIds;
    private final TIntHashSet threadIds;
    private final TIntHashSet messageIds;

    public SynchronizationCompletePacket(TIntHashSet forumIds, TIntHashSet threadIds, TIntHashSet messageIds) {
        this.forumIds = forumIds;
        this.messageIds = messageIds;
        this.threadIds = threadIds;
    }

    @Override
    public int[] getForumIds() {
        return forumIds.toArray();
    }

    @Override
    public int[] getMessageIds() {
        return messageIds.toArray();
    }

    public int[] getThreadIds() {
        return threadIds.toArray();
    }

    @Override
    public boolean isForumAffected(int forumId) {
        return forumIds.contains(forumId);
    }

    @Override
    public boolean isMessageAffected(int messageId) {
        return messageIds.contains(messageId);
    }

    public boolean isTopicAffected(int threadId) {
        return threadIds.contains(threadId);
    }
}
