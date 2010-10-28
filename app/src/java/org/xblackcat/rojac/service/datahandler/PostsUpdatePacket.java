package org.xblackcat.rojac.service.datahandler;

/**
 * @author xBlackCat
 */

public class PostsUpdatePacket implements IPacket {
    private final int[] messageIds;

    public PostsUpdatePacket(int[] messageIds) {
        this.messageIds = messageIds;
    }

    public int[] getMessageIds() {
        return messageIds;
    }

}
