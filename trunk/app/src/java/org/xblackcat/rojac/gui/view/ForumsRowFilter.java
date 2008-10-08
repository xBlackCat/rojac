package org.xblackcat.rojac.gui.view;

import org.xblackcat.rojac.gui.model.ForumTableModel;
import org.xblackcat.rojac.gui.model.ForumData;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ForumStatistic;

import javax.swing.*;

/**
 * Date: 8 זמגע 2008
*
* @author xBlackCat
*/
class ForumsRowFilter extends RowFilter<ForumTableModel, Integer> {
    private boolean subscribed;
    private boolean notEmpty;
    private boolean unread;

    @Override
    public boolean include(Entry<? extends ForumTableModel, ? extends Integer> entry) {
        if (!subscribed && !notEmpty && !unread) {
            return true;
        }
        ForumTableModel m = entry.getModel();
        int ind = entry.getIdentifier();

        ForumData fd = m.getValueAt(ind, 0);
        Forum f = fd.getForum();
        ForumStatistic fs = fd.getStat();

        boolean include = true;

        if (subscribed && f != null) {
            include &= f.isSubscribed();
        }

        if (fs != null) {
            include &= !unread || fs.getUnreadMessages() > 0;

            include &= !notEmpty || fs.getTotalMessages() > 0;
        }

        return include;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    public void setNotEmpty(boolean notEmpty) {
        this.notEmpty = notEmpty;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public boolean isNotEmpty() {
        return notEmpty;
    }

    public boolean isUnread() {
        return unread;
    }
}
