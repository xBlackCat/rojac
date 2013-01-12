package org.xblackcat.rojac.service.datahandler;

/**
 * @author xBlackCat
 */

public class SetSubThreadReadPacket extends SetForumReadPacket {
    protected final int postId;

    public SetSubThreadReadPacket(boolean readStatus, int forumId, int postId) {
        super(readStatus, forumId);
        this.postId = postId;
    }

    public int getPostId() {
        return postId;
    }
}
