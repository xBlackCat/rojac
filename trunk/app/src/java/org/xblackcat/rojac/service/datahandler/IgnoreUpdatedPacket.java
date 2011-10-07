package org.xblackcat.rojac.service.datahandler;

/**
 * @author xBlackCat
 */
public class IgnoreUpdatedPacket extends APacket {
    private final int threadId;
    private final int forumId;
    private final boolean ignored;

    public IgnoreUpdatedPacket(int forumId, int threadId, boolean ignored) {
        this.threadId = threadId;
        this.ignored = ignored;
        this.forumId = forumId;
    }

    public int getThreadId() {
        return threadId;
    }

    public boolean isIgnored() {
        return ignored;
    }

    public int getForumId() {
        return forumId;
    }
}
