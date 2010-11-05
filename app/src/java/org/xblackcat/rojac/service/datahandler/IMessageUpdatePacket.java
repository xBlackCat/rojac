package org.xblackcat.rojac.service.datahandler;

/**
 * @author xBlackCat
 */

public interface IMessageUpdatePacket extends IPacket {
    int[] getMessageIds();

    boolean isMessageAffected(int messageId);
}
