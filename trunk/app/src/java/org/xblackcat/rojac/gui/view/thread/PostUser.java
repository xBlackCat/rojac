package org.xblackcat.rojac.gui.view.thread;

/**
 * Delegate class to identify data set for a table renderer.
 *
* @author xBlackCat
*/
final class PostUser extends APostData {
    public PostUser(Post post) {
        super(post);
    }

    public String getUserName() {
        return post.getMessageData().getUserName();
    }

    public int getUserId() {
        return post.getMessageData().getUserId();
    }

}
