package org.xblackcat.rojac.gui.frame.mtree;

import javax.swing.table.AbstractTableModel;

/**
 * Date: 14 груд 2007
 *
 * @author xBlackCat
 */

public final class TreeTableModel extends AbstractTableModel {
    private MessageItem[] roots = MessageItem.NO_ITEMS;

    public void setRoots(MessageItem[] roots) {
        this.roots = roots;
        fireTableDataChanged();
    }

    public int getRowCount() {
        return getRowCount(roots);
    }

    public int getColumnCount() {
        return 0;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return getElementAt(rowIndex);
    }

    MessageItem getElementAt(int rowIndex) {
        return getItemAtRow(new int[]{rowIndex}, roots);
    }

    int getRowCount(MessageItem[] children) {
        int rows = 0;
        for (MessageItem mi : children) {
            rows += getRowCount(mi.getChildren()) + 1;
        }
        return rows;
    }

    MessageItem getItemAtRow(int[] row, MessageItem[] children) {
        for (MessageItem mi : children) {
            if (row[0] == 0) {
                return mi;
            } else {
                row[0]--;
                MessageItem res = getItemAtRow(row, mi.getChildren());
                if (res != null) {
                    return res;
                }
            }
        }
        return null;
    }

    public void setExpanded(MessageItem i, boolean b) {
        i.setExpanded(b);

        if (b) {

        }
    }
}
