package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.util.MessageUtils;

/**
 * Delegate class to identify replies amount on the post for a table renderer.
 *
 * @author xBlackCat
 */
final class PostRating extends APostProxy {
    public PostRating(Post post) {
        super(post);
    }

    @Override
    protected void setValue(PostTableCellRenderer renderer) {
        renderer.setIcon(
                MessageUtils.buildRateImage(
                        post.getMessageData().getRating(),
                        renderer.getFont(),
                        renderer.getForeground()
                )
        );
        renderer.setText(null);
    }
}