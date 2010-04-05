package org.xblackcat.rojac.gui.view.thread;

/**
 * Delegate class to identify replies amount on the post for a table renderer.
 *
 * @author xBlackCat
 */
final class PostReplies extends APostProxy {
    public PostReplies(Post post) {
        super(post);
    }

    @Override
    protected void setValue(PostTableCellRenderer renderer) {
        String replies;

        int repliesAmount = post.getRepliesAmount();

        if (repliesAmount == 0 && post.getThreadRoot() != post) {
            // Do not show zeroes for non-root posts.
            replies = "";
        } else {
            replies = String.valueOf(repliesAmount);
        }

        renderer.setText(replies);
    }
}