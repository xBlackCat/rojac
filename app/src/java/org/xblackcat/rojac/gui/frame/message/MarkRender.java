package org.xblackcat.rojac.gui.frame.message;

import org.xblackcat.rojac.data.Mark;

import javax.swing.*;
import java.awt.*;

/**
 * @author xBlackCat
 */

class MarkRender extends DefaultListCellRenderer {
    private final Icon defaultIcon;

    public MarkRender(Icon icon) {
        defaultIcon = icon;
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        Mark mark = (Mark) value;

        setText(null);
        if (mark == null) {
            setIcon(defaultIcon);
        } else {
            setIcon(mark.getIcon());
            if (index > -1) {
                list.setToolTipText(mark.toString());
            }
        }

        setHorizontalAlignment(SwingConstants.CENTER);
        return this;
    }
}
