package org.xblackcat.rojac.service.datahandler;

/**
 * @author xBlackCat
 */
public class IgnoreUpdatedPacket extends APacket {
    private final int threadId;
    private final boolean ignored;

    public IgnoreUpdatedPacket(int threadId, boolean ignored) {
        this.threadId = threadId;
        this.ignored = ignored;
    }

    public int getThreadId() {
        return threadId;
    }

    public boolean isIgnored() {
        return ignored;
    }
}
