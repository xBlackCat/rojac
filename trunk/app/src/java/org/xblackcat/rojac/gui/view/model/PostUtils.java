package org.xblackcat.rojac.gui.view.model;

import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.gui.theme.ReadStatusIcon;
import org.xblackcat.rojac.service.options.Property;

import javax.swing.*;

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

        if (Property.SKIP_IGNORED_USER_REPLY.get()) {
            if (post.getParent() != null && post.getParent().isIgnoredUser()) {
                return true;
            }
        }

        if (Property.SKIP_IGNORED_USER_THREAD.get()) {
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
        updateIgnoreUserFlag(model.getRoot(), userId, ignored);

        model.subTreeNodesChanged(model.getRoot());
    }

    private static void updateIgnoreUserFlag(Post root, int userId, boolean ignored) {
        if (root.getMessageData().getUserId() == userId && root.isIgnoredUser() != ignored) {
            root.setMessageData(root.getMessageData().setIgnoredUser(ignored));
        }

        for (Post p : root.childrenPosts) {
            updateIgnoreUserFlag(p, userId, ignored);
        }
    }

    public static Icon getPostIcon(Post post) {
        final int userId = Property.RSDN_USER_ID.get();

        boolean isOwnPost = post.getMessageData().getUserId() == userId;
        boolean isResponse = post.isReply(Property.RSDN_USER_ID.get());
        boolean hasUnreadReply = post.hasUnreadReply();

        ReadStatus readStatus = post.isRead();

        ReadStatusIcon iconHolder;

        if (isOwnPost) {
            if (post.getPostAmount() > 0) {
                iconHolder = ReadStatusIcon.HasResponse;
            } else {
                iconHolder = ReadStatusIcon.OwnPost;
            }
        } else if (isResponse) {
            iconHolder = ReadStatusIcon.Response;
        } else if (hasUnreadReply) {
            iconHolder = ReadStatusIcon.HasResponse;
        } else {
            iconHolder = ReadStatusIcon.Post;
        }

        return iconHolder.getIcon(readStatus);
    }

    public static Icon getPostIcon(MessageData data) {
        return getPostIcon(new Post(data, null));
    }
}
