package org.xblackcat.rojac.gui.frame.message;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.User;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.StorageException;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author xBlackCat
 */
class RateTableCellRenderer extends DefaultTableCellRenderer {
    private static final Log log = LogFactory.getLog(RateTableCellRenderer.class);

    private final IStorage storage = ServiceFactory.getInstance().getStorage();

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        MarkItem mi = (MarkItem) value;

        setIcon(mi.getMark().getIcon());
        // TODO: loads colors and fonts from the options
        if (mi.isNewRate()) {
            setBackground(Color.GREEN);
            setForeground(Color.LIGHT_GRAY);
            setText("<-- no -->");
        } else {
            setBackground(Color.WHITE);
            setForeground(Color.BLACK);
            try {
                User u = storage.getUserAH().getUserById(mi.getUserId());
                if (u != null) {
                    setText(u.getUserNick());
                } else {
                    setText("null");
                }
            } catch (StorageException e) {
                log.error("Can not load user info from storage", e);
            }
        }

        return this;
    }
}
