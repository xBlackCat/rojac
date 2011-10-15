package org.xblackcat.rojac.util;

import org.xblackcat.rojac.gui.view.model.Post;
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
}
