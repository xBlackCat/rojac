package org.xblackcat.sunaj.gui.frame.message;

import org.xblackcat.sunaj.data.NewRating;
import org.xblackcat.sunaj.data.Rating;

import javax.swing.table.AbstractTableModel;

/**
 * Date: 29 лют 2008
 *
 * @author xBlackCat
 */

class MarksTableModel extends AbstractTableModel {
    private static final MarkItem[] NO_MARKS = new MarkItem[0];

    private MarkItem[] rates = NO_MARKS;

    public void setData(Rating[] ratings, NewRating[] ownRatings) {
        MarkItem[] items = new MarkItem[ratings.length + ownRatings.length];

        int ind = 0;
        while (ind < ratings.length) {
            Rating r = ratings[ind];
            items[ind] = new MarkItem(r);
            ind++;
        }

        int i = 0;
        while (i < ownRatings.length) {
            NewRating r = ownRatings[i++];
            items[ind++] = new MarkItem(r);
        }

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
