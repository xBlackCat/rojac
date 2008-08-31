package org.xblackcat.rojac.gui.frame.mtree;

import org.xblackcat.rojac.data.Message;
import org.xblackcat.utils.ResourceUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Date: 15 груд 2007
 *
 * @author xBlackCat
 */
final class TopicCellRenderer extends MessageCellRenderer {
    private final Icon BRANCH_OPEN = ResourceUtils.loadImageIcon("/images/tree/treeExpanded.png");
    private final Icon BRANCH_CLOSED = ResourceUtils.loadImageIcon("/images/tree/treeCollapsed.png");

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