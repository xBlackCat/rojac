package org.xblackcat.rojac.service.datahandler;

/**
 * @author xBlackCat
 */

public class ThreadsUpdatePacket implements IThreadsUpdatePacket {
    private final int[] threadIds;

    public ThreadsUpdatePacket(int... threadIds) {
        this.threadIds = threadIds;
    }

    @Override
    public int[] getThreadIds() {
        return threadIds;
    }
}
