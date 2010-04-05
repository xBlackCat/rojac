package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.data.Mark;
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
        Mark[] ratings = post.getRating();
        renderer.setText(MessageUtils.buildRateString(ratings));
    }
}