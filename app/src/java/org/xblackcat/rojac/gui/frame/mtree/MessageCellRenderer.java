package org.xblackcat.rojac.gui.frame.mtree;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author xBlackCat
 */

abstract class MessageCellRenderer extends DefaultTableCellRenderer {
    // implements javax.swing.table.TableCellRenderer
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (setupComponent((MessageItem) value, isSelected)) {
            table.setRowHeight(row, super.getPreferredSize().height);
        }

        return this;
    }

    /**
     * Sets up the renderer component with right datas.
     *
     * @param item
     * @param selected @return <code>true</code> if the line should be resized.
     */
    protected abstract boolean setupComponent(MessageItem item, boolean selected);
}
