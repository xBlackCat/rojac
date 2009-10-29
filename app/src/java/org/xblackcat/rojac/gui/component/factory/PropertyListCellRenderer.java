package org.xblackcat.rojac.gui.component.factory;

import org.xblackcat.rojac.service.options.IValueChecker;

import javax.swing.*;
import java.awt.*;

/**
* @author xBlackCat
*/
class PropertyListCellRenderer extends DefaultListCellRenderer {
    private final IValueChecker checker;

    public PropertyListCellRenderer(IValueChecker checker) {
        this.checker = checker;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        setText(checker.getValueDescription(value));

        return this;
    }
}
