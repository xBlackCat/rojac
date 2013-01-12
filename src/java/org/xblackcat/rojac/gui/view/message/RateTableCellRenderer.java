package org.xblackcat.rojac.gui.view.message;

import org.xblackcat.rojac.data.User;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.util.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author xBlackCat
 */
class RateTableCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        MarkItem mi = (MarkItem) value;

        setBorder(null);

        if (mi.isNewRate()) {
            // TODO: loads colors and fonts from the options
            setForeground(UIUtils.brighter(getForeground(), .99));
        }

        if (column == 0) {
            setIcon(mi.getMarkIcons());
            setText(null);
            setHorizontalAlignment(CENTER);
            return this;
        }

        setIcon(null);
        setHorizontalAlignment(LEFT);
        if (mi.isNewRate()) {
            setText(Property.RSDN_USER_NAME.get());
        } else {
            User u = mi.getUser();
            if (u != null) {
                setText(u.getUserName());
            } else if (mi.getUserId().equals(Property.RSDN_USER_ID.get())) {
                setText(Property.RSDN_USER_NAME.get());
            } else {
                setText("User #" + mi.getUserId());
            }
        }

        return this;
    }
}
