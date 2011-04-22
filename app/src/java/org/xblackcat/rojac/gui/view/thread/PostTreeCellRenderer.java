package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.gui.view.model.Post;
import org.xblackcat.rojac.util.MessageUtils;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * @author xBlackCat
 */
class PostTreeCellRenderer extends DefaultTreeCellRenderer {
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

        Font font = getFont().deriveFont(style);
        setFont(font);

        setIcon(MessageUtils.getPostIcon(post));

        setText(post.getMessageData().getSubject());

        return this;
    }

}
