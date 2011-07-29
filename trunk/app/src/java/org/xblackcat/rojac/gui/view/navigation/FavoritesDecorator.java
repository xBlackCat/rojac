package org.xblackcat.rojac.gui.view.navigation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Favorite;
import org.xblackcat.rojac.data.FavoriteStatData;
import org.xblackcat.rojac.gui.theme.ReadStatusIcon;
import org.xblackcat.rojac.gui.view.model.FavoriteType;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.util.RojacWorker;

import java.util.Collection;
import java.util.List;

/**
 * Helper class to work only with favorites in navigation model
 *
 * @author xBlackCat Date: 22.07.11
 */
class FavoritesDecorator extends ADecorator {
    private static final Log log = LogFactory.getLog(FavoritesDecorator.class);

    private final AGroupItem<FavoriteItem> favorites;

    public FavoritesDecorator(AModelControl modelControl) {
        super(modelControl);
        favorites = new GroupItem<>(Message.View_Navigation_Item_Favorites, ReadStatusIcon.Favorite);
    }

    @Override
    AnItem[] getItemsList() {
        return new AnItem[]{
                favorites
        };
    }

    void reloadFavorites() {
        new FavoritesLoader().execute();
    }

    void updateFavoriteData(FavoriteType type) {
        int favoritesSize = favorites.getChildCount();

        int i = 0;
        while (i < favoritesSize) {
            FavoriteItem fd = favorites.getChild(i);
            Favorite f = fd.getFavorite();
            if (type == null || f.getType() == type) {
                new FavoriteInfoLoader(fd).execute();
            }
            i++;
        }
    }

    private void reload(Collection<Favorite> favoriteList) {
        modelControl.removeChildren(favorites);

        for (Favorite f : favoriteList) {
            modelControl.addChild(favorites, new FavoriteItem(favorites, f));
        }

        updateFavoriteData(null);
    }

    private class FavoritesLoader extends RojacWorker<Void, Favorite> {
        @Override
        protected Void perform() throws Exception {
            IStorage storage = ServiceFactory.getInstance().getStorage();

            for (Favorite f : storage.getFavoriteAH().getFavorites()) {
                publish(f);
            }

            return null;
        }

        @Override
        protected void process(List<Favorite> chunks) {
            reload(chunks);
        }
    }

    private class FavoriteInfoLoader extends RojacWorker<Void, Void> {
        private FavoriteStatData newStatistic;
        private String newName;
        private final FavoriteItem item;

        public FavoriteInfoLoader(FavoriteItem item) {
            this.item = item;
        }

        @Override
        protected Void perform() throws Exception {
            Favorite f = item.getFavorite();
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
            item.setStatistic(newStatistic);
            if (newName != null) {
                // Update favorite name
                item.setName(newName);
            }
        }

        @Override
        protected void done() {
            modelControl.itemUpdated(item);
        }
    }
}
