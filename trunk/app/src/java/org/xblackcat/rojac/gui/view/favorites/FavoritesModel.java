package org.xblackcat.rojac.gui.view.favorites;

import org.xblackcat.rojac.data.favorite.FavoriteType;
import org.xblackcat.rojac.data.favorite.IFavorite;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author xBlackCat
 */

public class FavoritesModel extends AbstractTableModel {
    private final List<IFavorite> favorites = new ArrayList<IFavorite>();

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return IFavorite.class;
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public int getRowCount() {
        return favorites.size();
    }

    @Override
    public IFavorite getValueAt(int rowIndex, int columnIndex) {
        return favorites.get(rowIndex);
    }

    public void updateFavoriteData(FavoriteType type) {
        int i = 0, favoritesSize = favorites.size();
        while (i < favoritesSize) {
            IFavorite f = favorites.get(i);
            if (type == null || f.getFavoriteType() == type) {
                f.updateStatistic(new PostUpdateAction(i));
            }
            i++;
        }
    }

    public void reload(IFavorite... favorites) {
        this.favorites.clear();

        this.favorites.addAll(Arrays.asList(favorites));
        fireTableDataChanged();

        updateFavoriteData(null);
    }

    private class PostUpdateAction implements Runnable {
        private final int id;

        public PostUpdateAction(int i) {
            id = i;
        }

        @Override
        public void run() {
            fireTableCellUpdated(id, 0);
        }
    }
}
