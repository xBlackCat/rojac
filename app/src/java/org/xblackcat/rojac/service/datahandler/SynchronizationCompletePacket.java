package org.xblackcat.rojac.service.datahandler;

import org.apache.commons.lang.ArrayUtils;

/**
 * @author xBlackCat
 */

public class SynchronizationCompletePacket implements IPacket, IForumUpdatePacket, IMessageUpdatePacket, IThreadsUpdatePacket {
    private final int[] forumIds;
    private final int[] threadIds;
    private final int[] messageIds;

    public SynchronizationCompletePacket(int[] forumIds, int[] threadIds, int[] messageIds) {
        this.forumIds = forumIds;
        this.messageIds = messageIds;
        this.threadIds = threadIds;
    }

    @Override
    public int[] getForumIds() {
        return forumIds;
    }

    @Override
    public int[] getMessageIds() {
        return messageIds;
    }

    public int[] getThreadIds() {
        return threadIds;
    }

    @Override
    public boolean isForumAffected(int forumId) {
        return ArrayUtils.contains(forumIds, forumId);
    }

    @Override
    public boolean isMessageAffected(int messageId) {
        return ArrayUtils.contains(messageIds, messageId);
    }
}
