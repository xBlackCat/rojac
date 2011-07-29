package org.xblackcat.rojac.gui.view.message;

import javax.swing.table.AbstractTableModel;

/**
 * @author xBlackCat
 */

class MarksTableModel extends AbstractTableModel {
    private static final MarkItem[] NO_MARKS = new MarkItem[0];

    private MarkItem[] rates = NO_MARKS;

    public void setData(MarkItem... items) {
        rates = items;
        fireTableDataChanged();
    }

    public int getRowCount() {
        return rates.length;
    }

    public int getColumnCount() {
        return 1;
    }

    public MarkItem getValueAt(int rowIndex, int columnIndex) {
        return rates[rowIndex];
    }

    public Class<?> getColumnClass(int columnIndex) {
        return MarkItem.class;
    }
}
