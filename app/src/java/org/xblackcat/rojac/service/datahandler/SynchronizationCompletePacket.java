package org.xblackcat.rojac.service.datahandler;

import gnu.trove.TIntHashSet;
import org.xblackcat.rojac.data.AffectedMessage;

/**
 * @author xBlackCat
 */

public class SynchronizationCompletePacket implements IPacket, IForumUpdatePacket, IMessageUpdatePacket, IThreadsUpdatePacket {
    private final TIntHashSet forumIds;
    private final TIntHashSet threadIds;
    private final TIntHashSet messageIds;

    public SynchronizationCompletePacket(TIntHashSet forumIds, TIntHashSet threadIds, TIntHashSet messageIds) {
        this.forumIds = (TIntHashSet) forumIds.clone();
        this.messageIds = (TIntHashSet) messageIds.clone();
        this.threadIds = (TIntHashSet) threadIds.clone();
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
