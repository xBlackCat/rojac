package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.gui.theme.IconPack;
import org.xblackcat.rojac.gui.theme.PostIcon;
import org.xblackcat.rojac.gui.view.model.Post;
import org.xblackcat.rojac.gui.view.model.ReadStatus;
import org.xblackcat.rojac.service.options.Property;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * @author xBlackCat
 */
class PostTreeCellRenderer extends DefaultTreeCellRenderer {
    private final IconPack imagePack = Property.ROJAC_GUI_ICONPACK.get();

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        Post post = (Post) value;
        int style;
        switch (post.isRead()) {
            default:
            case Read:
                style = Font.PLAIN;
                break;
            case ReadPartially:
                style = Font.PLAIN;
                break;
            case Unread:
                style = Font.BOLD;
                break;
        }

        PostIcon icon = getPostIcon(post);

        Font font = getFont().deriveFont(style);
        setFont(font);

        setIcon(imagePack.getIcon(icon));

        setText(post.getMessageData().getSubject());

        return this;
    }

    private static PostIcon getPostIcon(Post p) {
        boolean isOwnPost = p.getMessageData().getUserId() == Property.RSDN_USER_ID.get();
        boolean isResponse = p.getParent() != null && p.getParent().getMessageData().getUserId() == Property.RSDN_USER_ID.get();
        boolean hasResponse = p.getRepliesAmount() > 0;

        ReadStatus s = p.isRead();

        if (isOwnPost) {
            if (hasResponse) {
                return s == ReadStatus.Unread ? PostIcon.HasResponseUnread : PostIcon.HasResponse;
            } else {
                return s == ReadStatus.Unread ? PostIcon.OwnPostUnread : PostIcon.OwnPost;
            }
        }

        if (isResponse) {
            return s == ReadStatus.Unread ? PostIcon.UnreadResponse : PostIcon.Response;
        }

        switch (s) {
            case Read:
                return PostIcon.Read;
            case ReadPartially:
                return PostIcon.ReadPartially;
            case Unread:
                return PostIcon.Unread;
        }

        return PostIcon.Unread;
    }
}
