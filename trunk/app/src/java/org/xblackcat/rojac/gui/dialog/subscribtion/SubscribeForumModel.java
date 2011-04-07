package org.xblackcat.rojac.gui.dialog.subscribtion;

import org.xblackcat.rojac.data.Forum;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
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
        return 2;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnIndex == 0 ? Boolean.class : ForumInfo.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ForumInfo fi = data.get(rowIndex);

        return columnIndex == 0 ? fi.isSubscribed() : fi.getForum();
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
}
