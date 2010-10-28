package org.xblackcat.rojac.service.datahandler;

/**
 * @author xBlackCat
 */

public class ThreadsUpdatePacket implements IPacket {
    private final int[] threadIds;

    public ThreadsUpdatePacket(int... threadIds) {
        this.threadIds = threadIds;
    }

    public int[] getThreadIds() {
        return threadIds;
    }
}
