package org.xblackcat.rojac.service.datahandler;

/**
 * @author xBlackCat
 */

public class SetThreadReadPacket extends SetForumReadPacket {
    protected final int threadId;

    public SetThreadReadPacket(boolean readStatus, int forumId, int threadId) {
        super(readStatus, forumId);
        this.threadId = threadId;
    }

    public int getThreadId() {
        return threadId;
    }
}
