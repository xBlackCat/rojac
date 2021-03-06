package org.xblackcat.rojac.gui.view.model;

import org.xblackcat.rojac.gui.view.thread.PostTableCellRenderer;

/**
 * Delegate class to identify data set for a table renderer.
 *
 * @author xBlackCat
 */
final class PostDateRange extends APostProxy {
    public PostDateRange(Post post) {
        super(post);
    }

    @Override
    protected void setValue(PostTableCellRenderer renderer, boolean ignored) {
        renderer.setText("none");
    }
}
