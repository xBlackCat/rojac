package org.xblackcat.rojac.gui.view.message;

import org.apache.commons.lang3.ArrayUtils;
import org.xblackcat.rojac.data.Mark;

import javax.swing.*;

/**
 * @author xBlackCat
 */

class IconsModel extends AbstractListModel<Mark> implements ComboBoxModel<Mark> {
    private final Mark[] items;
    private Mark selectedItem;

    public IconsModel(Mark... items) {
        if (ArrayUtils.isEmpty(items)) {
            throw new IllegalArgumentException("Array is empty");
        }
        this.items = items;
        selectedItem = null;
    }

    public int getSize() {
        return items.length;
    }

    public Mark getElementAt(int index) {
        return items[index];
    }

    public Mark getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(Object selectedItem) {
        this.selectedItem = (Mark) selectedItem;
        fireContentsChanged(this, -1, -1);
    }

    public void reset() {
        setSelectedItem(null);
    }
}
