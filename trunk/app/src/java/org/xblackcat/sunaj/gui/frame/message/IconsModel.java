package org.xblackcat.sunaj.gui.frame.message;

import org.apache.commons.lang.ArrayUtils;
import org.xblackcat.sunaj.service.data.Mark;

import javax.swing.*;

/**
 * Date: 28 ñ³÷ 2008
 *
 * @author xBlackCat
 */

class IconsModel extends AbstractListModel implements ComboBoxModel {
    private final Mark[] items;
    private final Mark defaultItem;
    private Mark selectedItem;

    public IconsModel(Mark... items) {
        if (ArrayUtils.isEmpty(items)) {
            throw new IllegalArgumentException("Array is empty");
        }
        this.items = items;
        defaultItem = items[0];
        selectedItem = defaultItem;
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
        setSelectedItem(defaultItem);
    }

    public Mark getDefaultItem() {
        return defaultItem;
    }
}
