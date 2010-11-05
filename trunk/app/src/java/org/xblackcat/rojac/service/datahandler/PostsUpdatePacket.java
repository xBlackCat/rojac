package org.xblackcat.rojac.service.datahandler;

import org.apache.commons.lang.ArrayUtils;

/**
 * @author xBlackCat
 */

public class PostsUpdatePacket implements IMessageUpdatePacket {
    private final int[] messageIds;

    public PostsUpdatePacket(int[] messageIds) {
        this.messageIds = messageIds;
    }

    @Override
    public int[] getMessageIds() {
        return messageIds;
    }

    @Override
    public boolean isMessageAffected(int messageId) {
        return ArrayUtils.contains(messageIds, messageId);
    }
}
