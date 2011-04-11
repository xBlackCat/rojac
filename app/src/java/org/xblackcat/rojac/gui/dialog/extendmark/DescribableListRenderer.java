package org.xblackcat.rojac.gui.dialog.extendmark;

import javax.swing.*;
import java.awt.*;

/**
 * @author xBlackCat
 */

class DescribableListRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof IDescribable) {
            setText(((IDescribable) value).getLabel().get());
        }

        return this;
    }
}
