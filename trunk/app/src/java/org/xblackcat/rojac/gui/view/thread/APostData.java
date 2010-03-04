package org.xblackcat.rojac.gui.view.thread;

/**
 * @author xBlackCat
 */

public class APostData {
    protected final Post post;

    public APostData(Post post) {
        this.post = post;
    }

    public ReadStatus isRead() {
        return post.isRead();
    }
}
