package org.xblackcat.rojac.service.datahandler;

import gnu.trove.TIntHashSet;
import org.xblackcat.rojac.data.AffectedMessage;

/**
 * @author xBlackCat
 */

public class SetReadExPacket implements IPacket, IForumUpdatePacket, IMessageUpdatePacket, IThreadsUpdatePacket {
    private final boolean read;
    private final TIntHashSet forumIds;
    private final TIntHashSet threadIds;
    private final TIntHashSet messageIds;

    public SetReadExPacket(boolean read, Iterable<AffectedMessage> messages) {
        this.read = read;
        forumIds = new TIntHashSet();
        threadIds = new TIntHashSet();
        messageIds = new TIntHashSet();

        for (AffectedMessage m : messages) {
            forumIds.add(m.getForumId());
            threadIds.add(m.getTopicId());
            messageIds.add(m.getMessageId());
        }
    }

    public SetReadExPacket(TIntHashSet forumIds, TIntHashSet threadIds, TIntHashSet messageIds, boolean read) {
        this.forumIds = forumIds;
        this.messageIds = messageIds;
        this.threadIds = threadIds;
        this.read = read;
    }

    public SetReadExPacket(boolean read, TIntHashSet messageIds) {
        this.forumIds = new TIntHashSet();
        this.threadIds = new TIntHashSet();
        this.messageIds = messageIds;
        this.read = read;
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

    public boolean haveOnlyMessageIds() {
        return forumIds.isEmpty() && threadIds.isEmpty();
    }

    public boolean isTopicAffected(int threadId) {
        return threadIds.contains(threadId);
    }

    public boolean isRead() {
        return read;
    }
}
