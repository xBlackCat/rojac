package org.xblackcat.sunaj.gui.frame.thread;

import org.xblackcat.sunaj.data.Message;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * Date: 18 ρεπο 2008
 *
 * @author xBlackCat
 */
class MessageTreeCellRenderer extends DefaultTreeCellRenderer {
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        if (value != null) {
            Message mi = ((MessageItem) value).getMessage();

            if (mi != null) {
                setText(mi.getSubject() + "(" + mi.getUserNick() + ")");
            } else {
                setText("Forum root");
            }
        }

        setIcon(null);

        return this;
    }
}
