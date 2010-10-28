package org.xblackcat.rojac.service.datahandler;

/**
 * @author xBlackCat
 */

public class ForumsUpdatePacket implements IPacket {
    private final int[] forumIds;

    public ForumsUpdatePacket(int... forumIds) {
        this.forumIds = forumIds;
    }

    public int[] getForumIds() {
        return forumIds;
    }
}
