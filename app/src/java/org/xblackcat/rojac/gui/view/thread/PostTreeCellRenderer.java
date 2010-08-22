package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.gui.theme.IconPack;
import org.xblackcat.rojac.gui.theme.PostIcon;
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
        PostIcon icon;
        switch (post.isRead()) {
            default:
            case Read:
                style = Font.PLAIN;
                icon = PostIcon.Read;
                break;
            case ReadPartially:
                style = Font.ITALIC;
                icon = PostIcon.ReadPartially;
                break;
            case Unread:
                style = Font.BOLD | Font.ITALIC;
                icon = PostIcon.Unread;
                break;
        }

        if (post.getMessageData().getUserId() == Property.RSDN_USER_ID.get()) {
            // Own message - own icon.
            icon = PostIcon.OwnPost;

            if (post.getSize() > 0) {
                icon = PostIcon.HasResponse;
            }
        }

        Font font = getFont().deriveFont(style);
        setFont(font);

        setIcon(imagePack.getIcon(icon));

        setText(post.getMessageData().getSubject());

        return this;
    }
}
