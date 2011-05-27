package org.xblackcat.rojac.service.datahandler;

/**
 * @author xBlackCat
 */

public class SetForumReadPacket implements IPacket {
    protected final int forumId;
    protected final boolean read;

    public SetForumReadPacket(boolean read, int forumId) {
        this.forumId = forumId;
        this.read = read;
    }

    public int getForumId() {
        return forumId;
    }

    public boolean isRead() {
        return read;
    }
}
