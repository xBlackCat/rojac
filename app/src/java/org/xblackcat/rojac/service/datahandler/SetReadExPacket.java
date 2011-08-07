package org.xblackcat.rojac.service.datahandler;

import gnu.trove.set.hash.TIntHashSet;
import org.xblackcat.rojac.data.AffectedMessage;
import org.xblackcat.rojac.data.MessageData;

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

    public SetReadExPacket(Iterable<MessageData> messages, boolean read) {
        this.read = read;
        forumIds = new TIntHashSet();
        threadIds = new TIntHashSet();
        messageIds = new TIntHashSet();

        for (MessageData m : messages) {
            forumIds.add(m.getForumId());
            threadIds.add(m.getThreadRootId());
            messageIds.add(m.getMessageId());
        }
    }

    public SetReadExPacket(int forumId, int threadId, TIntHashSet messageIds, boolean read) {
        this.forumIds = new TIntHashSet();
        this.threadIds = new TIntHashSet();
        this.messageIds = messageIds;
        this.read = read;

        forumIds.add(forumId);
        threadIds.add(threadId);
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

    @Override
    public boolean isTopicAffected(int threadId) {
        return threadIds.contains(threadId);
    }

    public boolean isRead() {
        return read;
    }
}
