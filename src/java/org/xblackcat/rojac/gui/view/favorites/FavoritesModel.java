package org.xblackcat.rojac.gui.view.favorites;

import org.xblackcat.rojac.data.Favorite;
import org.xblackcat.rojac.data.ReadStatistic;
import org.xblackcat.rojac.gui.view.model.FavoriteType;
import org.xblackcat.rojac.service.executor.TaskType;
import org.xblackcat.rojac.service.executor.TaskTypeEnum;
import org.xblackcat.rojac.util.RojacWorker;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author xBlackCat
 */

class FavoritesModel extends AbstractTableModel {
    private final List<FavoriteData> favorites = new ArrayList<>();

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return FavoriteData.class;
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
    public FavoriteData getValueAt(int rowIndex, int columnIndex) {
        return favorites.get(rowIndex);
    }

    public void updateFavoriteData(FavoriteType type) {
        int favoritesSize = favorites.size();

        int i = 0;
        while (i < favoritesSize) {
            FavoriteData fd = favorites.get(i);
            Favorite f = fd.getFavorite();
            if (type == null || f.getType() == type) {
                new FavoriteInfoLoader(fd, i).execute();
            }
            i++;
        }
    }


    public void reload(Collection<Favorite> favorites) {
        this.favorites.clear();

        for (Favorite f : favorites) {
            this.favorites.add(new FavoriteData(f));
        }
        fireTableDataChanged();

        updateFavoriteData(null);
    }

    @TaskType(TaskTypeEnum.Initialization)
    private class FavoriteInfoLoader extends RojacWorker<Void, Void> {
        private ReadStatistic newStatistic;
        private String newName;
        private final FavoriteData favoriteData;
        private final int rowId;

        public FavoriteInfoLoader(FavoriteData favoriteData, int rowId) {
            this.favoriteData = favoriteData;
            this.rowId = rowId;
        }

        @Override
        protected Void perform() throws Exception {
            Favorite f = favoriteData.getFavorite();
            FavoriteType type = f.getType();
            int itemId = f.getItemId();

            newStatistic = type.loadStatistic(itemId);
            if (!f.isNameSet()) {
                newName = type.loadName(itemId);
            }

            publish();
            return null;
        }

        @Override
        protected void process(List<Void> chunks) {
            favoriteData.setStatistic(newStatistic);
            if (newName != null) {
                // Update favorite name
                favoriteData.setName(newName);
            }
        }

        @Override
        protected void done() {
            fireTableRowsUpdated(rowId, rowId);
        }
    }

}
