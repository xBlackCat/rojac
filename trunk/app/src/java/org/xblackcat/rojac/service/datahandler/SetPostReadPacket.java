package org.xblackcat.rojac.service.datahandler;

import org.xblackcat.rojac.data.MessageData;

/**
 * @author xBlackCat
 */

public class SetPostReadPacket implements IPacket {
    protected final MessageData post;
    private final boolean readStatus;

    /**
     * Constructs a post read status changed packet.
     *
     * @param post
     * @param readStatus
     */
    public SetPostReadPacket(MessageData post, boolean readStatus) {
        this.post = post;
        this.readStatus = readStatus;
    }

    public MessageData getPost() {
        return post;
    }

    public boolean isRead() {
        return readStatus;
    }
}
