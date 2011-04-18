package org.xblackcat.rojac.gui.dialog.subscribtion;

import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.service.datahandler.SubscriptionChanged;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author xBlackCat
 */
class SubscribeForumModel extends AbstractTableModel {
    private final List<ForumInfo> data = new ArrayList<ForumInfo>();

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "";
            case 1:
                return "Forum";
            case 2:
                return "Long name";
        }

        return "";
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnIndex == 0 ? Boolean.class : String.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ForumInfo fi = data.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return fi.isSubscribed();
            case 1:
                return fi.getForum().getShortForumName();
            default:
                return fi.getForum().getForumName();
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            data.get(rowIndex).setSubscribed((Boolean) aValue);
        }
    }

    public void addForums(List<Forum> forums) {
        for (Forum f : forums) {
            data.add(new ForumInfo(f));
        }

        fireTableDataChanged();
    }

    public void getChanged() {
    }

    /**
     * Returns a packet with filled new subscription state for forums.
     *
     * @return a filled packet or <code>null</code> if no subscription states are changed.
     */
    public SubscriptionChanged getSubscription() {
        Collection<SubscriptionChanged.Subscription> subscriptions = new LinkedList<SubscriptionChanged.Subscription>();

        for (ForumInfo fi : data) {
            if (fi.getSubscribeStatus() != null) {
                subscriptions.add(new SubscriptionChanged.Subscription(fi.getForum().getForumId(), fi.isSubscribed()));
            }
        }

        return new SubscriptionChanged(subscriptions);
    }
}
