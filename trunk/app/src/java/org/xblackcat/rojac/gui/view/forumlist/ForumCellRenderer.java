package org.xblackcat.rojac.gui.view.forumlist;

import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ForumStatistic;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Renterer for a forum view list cell.
 * <p/>
 *
 * @author xBlackCat
 */
class ForumCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        ForumData fd = (ForumData) value;

        Forum f = fd.getForum();
        ForumStatistic fs = fd.getStat();

        boolean hasUnread = fs != null && fs.getUnreadMessages() > 0;

        StringBuilder text = new StringBuilder("<html><body><div style='overflow: hidden;'>");
        boolean isNotSubcribed = true;
        if (f != null) {
            isNotSubcribed = !f.isSubscribed();

            if (isNotSubcribed) {
                text.append("<i>");
            }
            if (hasUnread) {
                text.append("<b>");
            }
            text.append(f.getForumName());
            if (hasUnread) {
                text.append("</b>");
            }
            if (isNotSubcribed) {
                text.append("</i>");
            }

            if (fs != null) {
                text.append(" (");
                text.append(fs.getUnreadMessages());
                text.append("/");
                text.append(fs.getTotalMessages());
                text.append(")");
            }

        } else {
            text.append("<i>Loading info for forum: ");
            text.append(fd.getForumId());
        }
        setText(text.toString());
        table.setToolTipText(text.toString());
        setIcon(null);
        Color bgc;
        if (isNotSubcribed) {
            bgc = new Color(0xFFFF7F);
        } else {
            bgc = new Color(0x7FFF7F);
        }
        setBackground(bgc);

        return this;
    }
}
