package org.xblackcat.rojac.gui.view.thread;

/**
 * Delegate class to identify data set for a table renderer.
 *
 * @author xBlackCat
 */
final class PostId extends APostProxy {
    public PostId(Post post) {
        super(post);
    }

    @Override
    protected void setValue(PostTableCellRenderer renderer) {
        renderer.setText(String.valueOf(post.getMessageData().getMessageId()));
    }
}