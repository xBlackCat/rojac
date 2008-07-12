package org.xblackcat.sunaj.gui.render;

import org.xblackcat.sunaj.gui.model.ForumViewMode;

import javax.swing.*;
import java.awt.*;

/**
 * Date: 12 лип 2008
*
* @author xBlackCat
*/
public class ForumListModeRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        ForumViewMode m = (ForumViewMode) value;

        setText(m.getText());
        setToolTipText(m.getTooltip());

        return this;
    }
}
