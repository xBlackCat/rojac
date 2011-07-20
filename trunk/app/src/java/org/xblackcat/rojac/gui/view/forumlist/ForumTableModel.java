package org.xblackcat.rojac.gui.view.forumlist;

import org.xblackcat.rojac.data.ForumStatistic;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.IStorage;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xBlackCat
 */

class ForumTableModel extends AbstractTableModel {
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

    void fillForums(List<ForumData> forums) {
        this.forums.clear();

        this.forums.addAll(forums);

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

    public void updateStatistic(ForumStatistic stat) {
        int idx = getForumDataIndex(stat.getForumId());
        if (idx != -1) {
            ForumData fd = forums.get(idx);
            fd.setStat(stat);
            fireTableRowsUpdated(idx, idx);
        }
    }

}
