package org.xblackcat.rojac.service.datahandler;

/**
 * @author xBlackCat
 */

public interface IThreadsUpdatePacket extends IPacket {
    int[] getThreadIds();

    boolean isTopicAffected(int threadId);
}
