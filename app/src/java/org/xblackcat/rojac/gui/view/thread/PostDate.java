package org.xblackcat.rojac.gui.view.thread;

/**
 * Delegate class to identify data set for a table renderer.
 *
 * @author xBlackCat
 */
final class PostDate extends APostData {
    public PostDate(Post post) {
        super(post);
    }

    public long getPostDate() {
        return post.getMessageData().getMessageDate();
    }
}