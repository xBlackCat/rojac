package org.xblackcat.rojac.gui.view.startpage;

import org.apache.commons.collections.CollectionUtils;
import org.xblackcat.rojac.util.RojacUtils;

import javax.swing.table.AbstractTableModel;
import java.util.Collection;

/**
 * @author xBlackCat
 */

class RecentThreadsModel extends AbstractTableModel {
    public static final LastPostInfo[] EMPTY_LIST = new LastPostInfo[0];

    private LastPostInfo[] lastPosts = EMPTY_LIST;
    private boolean loading = false;

    @Override
    public int getRowCount() {
        return lastPosts.length;
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return LastPostInfo.class;
    }

    @Override
    public LastPostInfo getValueAt(int rowIndex, int columnIndex) {
        return lastPosts[rowIndex];
    }

    public void clear() {
        assert RojacUtils.checkThread(true);

        lastPosts = EMPTY_LIST;
        loading = true;

        fireTableDataChanged();
    }

    public boolean isLoading() {
        assert RojacUtils.checkThread(true);

        return loading;
    }

    public void markAsLoaded() {
        assert RojacUtils.checkThread(true);

        loading = false;
    }

    public void addLatestPosts(Collection<LastPostInfo> posts) {
        int firstRow = lastPosts.length;

        if (CollectionUtils.isNotEmpty(posts)) {
            LastPostInfo[] newList = new LastPostInfo[lastPosts.length + posts.size()];
            System.arraycopy(lastPosts, 0, newList, 0, lastPosts.length);

            int i = firstRow;
            for (LastPostInfo p : posts) {
                newList[i++] = p;
            }

            lastPosts = newList;
            fireTableRowsInserted(firstRow, lastPosts.length - 1);
        }
    }
}
