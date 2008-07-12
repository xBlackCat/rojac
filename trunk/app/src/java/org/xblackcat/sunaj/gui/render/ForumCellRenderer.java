package org.xblackcat.sunaj.gui.render;

import org.xblackcat.sunaj.data.Forum;

import javax.swing.*;
import java.awt.*;

/**
 * Date: 12 лип 2008
*
* @author xBlackCat
*/
public class ForumCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        Forum f = (Forum) value;

        setText(f.getForumName());

        return this;
    }
}
