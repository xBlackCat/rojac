package org.xblackcat.rojac.gui.frame.mtree;

import org.xblackcat.rojac.data.Message;
import org.xblackcat.rojac.service.RojacHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * @author xBlackCat
 */
final class TopicCellRenderer extends MessageCellRenderer {
    private final Icon BRANCH_OPEN = RojacHelper.loadIcon("tree/treeExpanded.png");
    private final Icon BRANCH_CLOSED = RojacHelper.loadIcon("tree/treeCollapsed.png");

    protected boolean setupComponent(MessageItem item, boolean selected) {
        setIcon(item.isExpanded() ? BRANCH_OPEN : BRANCH_CLOSED);
        setBorder(new EmptyBorder(0, Utils.getTopicShift(item), 0, 0));

        Message m = item.getMessage();
        if (m != null) {
            m.getSubject();
        } else {
            setText("Updating...");
        }

        return false;
    }
}