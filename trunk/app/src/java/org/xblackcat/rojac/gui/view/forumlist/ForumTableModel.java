package org.xblackcat.rojac.gui.view.forumlist;

import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ForumStatistic;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.IStorage;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author xBlackCat
 */

public class ForumTableModel extends AbstractTableModel {
    protected final IStorage storage = ServiceFactory.getInstance().getStorage();

    private List<ForumData> forums = new ArrayList<ForumData>();

    public ForumData getValueAt(int rowIndex, int columnIndex) {
        return forums.get(rowIndex);
    }

    public int getColumnCount() {
        return 1;
    }

    public int getRowCount() {
        return forums.size();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return ForumData.class;
    }

    void fillForums(Collection<Forum> forums) {
        this.forums.clear();

        for (Forum forum : forums) {
            this.forums.add(new ForumData(forum));
        }

        fireTableDataChanged();
    }

    public ForumData getForumData(int forumId) {
        int idx = getForumDataIndex(forumId);
        if (idx != -1) {
            return forums.get(idx);
        } else {
            return null;
        }
    }

    private int getForumDataIndex(int forumId) {
        for (int i = 0, forumsSize = forums.size(); i < forumsSize; i++) {
            ForumData fd = forums.get(i);
            if (fd.getForumId() == forumId) {
                return i;
            }
        }

        return -1;
    }

    public void setSubscribed(int forumId, boolean subscribed) {
        int idx = getForumDataIndex(forumId);
        ForumData fd = forums.get(idx);
        fd.setSubscribed(subscribed);
        fireTableRowsUpdated(idx, idx);
    }

    public void setRead(boolean read, int... forumIds) {
        for (int forumId : forumIds) {
            int idx = getForumDataIndex(forumId);
            ForumData fd = forums.get(idx);
            ForumStatistic oldStatistic = fd.getStat();
            final ForumStatistic newStatistic = new ForumStatistic(
                    forumId,
                    oldStatistic.getTotalMessages(),
                    read ? 0 : oldStatistic.getTotalMessages(),
                    oldStatistic.getLastMessageDate());
            fd.setStat(newStatistic);
            fireTableRowsUpdated(idx, idx);
        }
    }

    public void updateStatistic(ForumStatistic stat) {
        int idx = getForumDataIndex(stat.getForumId());
        if (idx != -1) {
            ForumData fd = forums.get(idx);
            fd.setStat(stat);
            fireTableRowsUpdated(idx, idx);
        }
    }
}
