package org.xblackcat.rojac.gui.render;

import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ForumStatistic;
import org.xblackcat.rojac.gui.model.ForumData;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Renterer for a forum view list cell.
 * <p/>
 * Date: 12 лип 2008
 *
 * @author xBlackCat
 */
public class ForumCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        ForumData fd = (ForumData) value;

        Forum f = fd.getForum();
        ForumStatistic fs = fd.getStat();

        boolean hasUnread = fs != null && fs.getUnreadMessages() > 0;

        StringBuilder text = new StringBuilder("<html><body>");
        if (f != null) {
            boolean isNotSubcribed = !f.isSubscribed();

            if (isNotSubcribed) text.append("<i>");
            if (hasUnread) text.append("<b>");
            text.append(f.getForumName());
            if (hasUnread) text.append("</b>");
            if (isNotSubcribed) text.append("</i>");

            if (fs != null) {
                text.append(" (");
                text.append(fs.getUnreadMessages());
                text.append("/");
                text.append(fs.getTotalMessages());
                text.append(")");
            }

            Color bgc;
            if (isNotSubcribed) {
                bgc = new Color(0xFFFF7F);
            } else {
                bgc = new Color(0x7FFF7F);
            }
            setBackground(bgc);
        } else {
            text.append("<i>Loading info for forum: ");
            text.append(fd.getForumId());
        }
        setText(text.toString());
        setIcon(null);

        return this;
    }
}
