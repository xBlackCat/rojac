package org.xblackcat.rojac.gui.view.message;

import javax.swing.table.AbstractTableModel;

/**
 * @author xBlackCat
 */

class RatingListModel extends AbstractTableModel {
    public static final MarkItem[] NO_MARKS = new MarkItem[0];

    private MarkItem[] rates = NO_MARKS;

    public void setData(MarkItem... items) {
        rates = items;
        fireTableDataChanged();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return MarkItem.class;
    }

    @Override
    public int getRowCount() {
        return rates.length;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return rates[rowIndex];
    }
}
