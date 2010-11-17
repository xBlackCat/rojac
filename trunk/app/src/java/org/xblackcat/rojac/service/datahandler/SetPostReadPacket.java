package org.xblackcat.rojac.service.datahandler;

/**
 * @author xBlackCat
 */

public class SetPostReadPacket extends SetForumReadPacket {
    protected final int postId;
    protected final boolean recursive;

    public SetPostReadPacket(boolean readStatus, int forumId, int postId, boolean recursive) {
        super(readStatus, forumId);
        this.postId = postId;
        this.recursive = recursive;
    }

    public int getPostId() {
        return postId;
    }

    public boolean isRecursive() {
        return recursive;
    }
}
