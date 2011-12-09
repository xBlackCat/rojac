package org.xblackcat.rojac.gui.view.model;

import org.xblackcat.rojac.service.options.Property;

/**
 * @author xBlackCat
 */
public final class PostUtils {
    private PostUtils() {
    }

    public static boolean isPostIgnoredByUser(Post post) {
        if (post == null) {
            return false;
        }

        if (post.isIgnoredUser()) {
            return true;
        }

        if (Property.SKIP_IGNORED_USER_REPLY.get(false)) {
            if (post.getParent() != null && post.getParent().isIgnoredUser()) {
                return true;
            }
        }

        if (Property.SKIP_IGNORED_USER_THREAD.get(false)) {
            Post parent;
            while ((parent = post.getParent()) != null) {
                if (parent.isIgnoredUser()) {
                    return true;
                }
                post = parent;
            }
        }

        return false;
    }

    public static void setIgnoreUserFlag(SortedThreadsModel model, int userId, boolean ignored) {
        updateIgnoreUserFlag(model, model.getRoot(), userId, ignored);

        model.subTreeNodesChanged(model.getRoot());
    }

    private static void updateIgnoreUserFlag(SortedThreadsModel model, Post root, int userId, boolean ignored) {
        if (root.getMessageData().getUserId() == userId && root.isIgnoredUser() != ignored) {
            root.setMessageData(root.getMessageData().setIgnoredUser(ignored));
        }

        for (Post p : root.childrenPosts) {
            updateIgnoreUserFlag(model, p, userId, ignored);
        }
    }
}
