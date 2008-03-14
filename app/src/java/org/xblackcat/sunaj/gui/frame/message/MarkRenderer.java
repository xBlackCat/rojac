package org.xblackcat.sunaj.gui.frame.message;

import org.xblackcat.sunaj.service.data.Mark;

import javax.swing.*;
import java.awt.*;

/**
 * Date: 28 ñ³÷ 2008
 *
 * @author xBlackCat
 */

class MarkRenderer extends DefaultListCellRenderer {
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        Mark mark = (Mark) value;

        if (mark == null) {
            throw new NullPointerException("Item became null");
        }

        setText(null);
        setIcon(mark.getIcon());
        if (index > -1) {
            list.setToolTipText(mark.toString());
        }

        return this;
    }
}
