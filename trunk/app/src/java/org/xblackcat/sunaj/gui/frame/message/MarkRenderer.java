package org.xblackcat.sunaj.gui.frame.message;

import org.xblackcat.sunaj.data.Mark;

import javax.swing.*;
import java.awt.*;

/**
 * Date: 28 ñ³÷ 2008
 *
 * @author xBlackCat
 */

class MarkRenderer extends DefaultListCellRenderer {
    private final Icon defaultIcon;

    public MarkRenderer(Icon icon) {
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
        return this;
    }
}
