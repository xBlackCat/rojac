package org.xblackcat.rojac.gui.view.thread;

/**
 * Delegate class to identify data set for a table renderer.
 *
 * @author xBlackCat
 */
final class PostId extends APostData {
    public PostId(Post post) {
        super(post);
    }

    public int getPostId() {
        return post.getMessageId();
    }
}