package org.xblackcat.rojac.gui.view.message;

import org.xblackcat.rojac.data.User;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.util.UIUtils;

import javax.swing.*;
import java.awt.*;

/**
 * @author xBlackCat
 */
class RateTableCellRenderer extends DefaultListCellRenderer {
    private static final Color LIGHT_GREEN = new Color(127, 255, 127);

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        MarkItem mi = (MarkItem) value;

        setIcon(mi.getMark().getIcon());
        // TODO: loads colors and fonts from the options
        if (mi.isNewRate()) {
            setForeground(UIUtils.brighter(getForeground(), .99));
            setText(Property.RSDN_USER_NAME.get());
        } else {
            User u = mi.getUser();
            if (u != null) {
                setText(u.getUserNick());
            } else if (mi.getUserId().equals(Property.RSDN_USER_ID.get())) {
                setText(Property.RSDN_USER_NAME.get());
            } else {
                setText("User #" + mi.getUserId());
            }
        }

        return this;
    }
}
