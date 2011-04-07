package org.xblackcat.rojac.gui.dialog.options;

import org.xblackcat.rojac.service.options.IValueChecker;

import javax.swing.*;

/**
* @author xBlackCat
*/
class ComboBoxEditorModel extends AbstractListModel implements ComboBoxModel {
    private final IValueChecker checker;
    private Object selected;

    @SuppressWarnings({"unchecked"})
    public ComboBoxEditorModel(IValueChecker checker, Object selected) {
        this.checker = checker;
        if (selected != null && checker.isValueCorrect(selected)) {
            this.selected = selected;
        } else {
            // Automatically select the first component
            this.selected = getElementAt(0);
        }
    }

    @Override
    public int getSize() {
        return checker.getPossibleValues().length;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public void setSelectedItem(Object anItem) {
        if (checker.isValueCorrect(anItem)) {
            selected = anItem;
            fireContentsChanged(this, -1, -1);
        }
    }

    @Override
    public Object getElementAt(int index) {
        return checker.getPossibleValues()[index];
    }

    @Override
    public Object getSelectedItem() {
        return selected;
    }
}
