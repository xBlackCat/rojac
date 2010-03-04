package org.xblackcat.rojac.gui.view.thread;

/**
 * Delegate class to identify data set for a table renderer.
 *
* @author xBlackCat
*/
final class PostUser extends APostProxy {
    public PostUser(Post post) {
        super(post);
    }

    @Override
    protected void setValue(PostTableCellRenderer renderer) {
        renderer.setText(post.getMessageData().getUserName());
    }
}
