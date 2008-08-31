package org.xblackcat.rojac.gui.model;

import javax.swing.*;

/**
 * Date: 12 лип 2008
 *
 * @author xBlackCat
 */
public class ForumViewModeModel extends AbstractListModel implements ComboBoxModel {
    private ForumViewMode selected = ForumViewMode.SHOW_ALL;

    public int getSize() {
        return ForumViewMode.values().length;
    }

    public ForumViewMode getElementAt(int index) {
        return ForumViewMode.values()[index];
    }

    public void setSelectedItem(Object anObject) {
        if ((selected != null && !selected.equals(anObject)) ||
                selected == null && anObject != null) {
            selected = (ForumViewMode) anObject;
            fireContentsChanged(this, -1, -1);
        }
    }

    public ForumViewMode getSelectedItem() {
        return selected;
    }
}
