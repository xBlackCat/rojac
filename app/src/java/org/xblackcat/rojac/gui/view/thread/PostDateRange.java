package org.xblackcat.rojac.gui.view.thread;

import gnu.trove.TLongArrayList;

/**
 * Delegate class to identify data set for a table renderer.
 *
 * @author xBlackCat
 */
final class PostDateRange extends APostData {
    public PostDateRange(Post post) {
        super(post);
    }

    public long getPostDate() {
        return post.getMessageData().getMessageDate();
    }

    public long[] getRange() {
        int size = post.getSize();
        if (size == 0) {
            return new long[]{post.getMessageData().getMessageDate()};
        }

        if (post.getLoadingState() != LoadingState.Loaded) {
            return new long[]{
                    post.getMessageData().getMessageDate(),
                    post.getLastPostDate()
            };
        }

        TLongArrayList range = new TLongArrayList(size * 2 + 2);
        range.add(post.getMessageData().getMessageDate());
        for (int i = 0; i < size; i++) {
            Post child = post.getChild(i);

            range.add(child.getMessageData().getMessageDate());
            range.add(child.getLastPostDate());
        }

        return range.toNativeArray();
    }
}