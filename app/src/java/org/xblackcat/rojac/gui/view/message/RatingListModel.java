package org.xblackcat.rojac.gui.view.message;

import javax.swing.*;

/**
 * @author xBlackCat
 */

class RatingListModel extends AbstractListModel<MarkItem> {
    private static final MarkItem[] NO_MARKS = new MarkItem[0];

    private MarkItem[] rates = NO_MARKS;

    public void setData(MarkItem... items) {
        if (rates.length > 0) {
            fireIntervalRemoved(this, 0, rates.length - 1);
        }
        rates = items;
        if (rates.length > 0) {
            fireIntervalAdded(this, 0, rates.length - 1);
        }
    }

    @Override
    public int getSize() {
        return rates.length;
    }

    @Override
    public MarkItem getElementAt(int index) {
        return rates[index];
    }
}
