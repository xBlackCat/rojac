package org.xblackcat.rojac.service.datahandler;

/**
 * @author xBlackCat
 */

public class SetPostReadPacket extends SetForumReadPacket {
    protected final int postId;

    /**
     * Constructs a post read status changed packet.
     *
     * @param readStatus
     * @param forumId
     * @param postId
     */
    public SetPostReadPacket(boolean readStatus, int forumId, int postId) {
        super(readStatus, forumId);
        this.postId = postId;
    }

    public int getPostId() {
        return postId;
    }

}
