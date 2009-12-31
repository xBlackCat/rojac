package org.xblackcat.rojac.gui.view.message;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.User;
import org.xblackcat.rojac.service.options.Property;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author xBlackCat
 */
class RateTableCellRenderer extends DefaultTableCellRenderer {
    private static final Log log = LogFactory.getLog(RateTableCellRenderer.class);
    private static final Color LIGHT_GREEN = new Color(127, 255, 127);

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        MarkItem mi = (MarkItem) value;

        setIcon(mi.getMark().getIcon());
        // TODO: loads colors and fonts from the options
        if (mi.isNewRate()) {
            setBackground(LIGHT_GREEN);
            setForeground(Color.BLACK);
            setText(Property.RSDN_USER_NAME.get());
        } else {
            setBackground(Color.WHITE);
            setForeground(Color.BLACK);
            User u = mi.getUser();
            if (u != null) {
                setText(u.getUserNick());
            } else {
                setText("User #" + mi.getUserId());
            }
        }

        return this;
    }
}
