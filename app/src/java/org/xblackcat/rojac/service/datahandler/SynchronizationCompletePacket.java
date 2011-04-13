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

    public SynchronizationCompletePacket(Iterable<AffectedMessage> messages) {
        forumIds = new TIntHashSet();
        threadIds = new TIntHashSet();
        messageIds = new TIntHashSet();

        for (AffectedMessage m : messages) {
            forumIds.add(m.getForumId());
            threadIds.add(m.getTopicId());
            messageIds.add(m.getMessageId());
        }
    }

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
