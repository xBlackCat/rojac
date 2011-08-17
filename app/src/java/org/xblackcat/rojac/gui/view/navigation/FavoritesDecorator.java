package org.xblackcat.rojac.gui.view.navigation;

import org.xblackcat.rojac.data.Favorite;
import org.xblackcat.rojac.data.FavoriteStatData;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.gui.theme.ReadStatusIcon;
import org.xblackcat.rojac.gui.view.model.FavoriteType;
import org.xblackcat.rojac.i18n.Message;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Helper class to work only with favorites in navigation model
 *
 * @author xBlackCat Date: 22.07.11
 */
class FavoritesDecorator extends ADecorator {
    private final AGroupItem<FavoriteItem> favorites;

    public FavoritesDecorator(IModelControl modelControl) {
        super(modelControl);
        favorites = new GroupItem<>(Message.View_Navigation_Item_Favorites, ReadStatusIcon.Favorite);
    }

    @Override
    AnItem[] getItemsList() {
        return new AnItem[]{
                favorites
        };
    }

    ALoadTask[] reloadFavorites() {
        return ALoadTask.group(new FavoritesLoadTask());
    }

    ALoadTask[] updateFavoriteData(FavoriteType type) {
        int favoritesSize = favorites.getChildCount();
        Collection<ALoadTask<Stat>> tasks = new ArrayList<>(favoritesSize);

        int i = 0;
        while (i < favoritesSize) {
            FavoriteItem fd = favorites.getChild(i);
            Favorite f = fd.getFavorite();
            if (type == null || f.getType() == type) {
                tasks.add(new FavoriteStatLoadTask(fd));
            }
            i++;
        }

        return tasks.toArray(new ALoadTask[tasks.size()]);
    }

    public ALoadTask[] alterReadStatus(MessageData post, boolean read) {
        int favoritesSize = favorites.getChildCount();
        Collection<ALoadTask> tasks = new ArrayList<>(favoritesSize);

        int i = 0;
        while (i < favoritesSize) {
            FavoriteItem fd = favorites.getChild(i);
            Favorite f = fd.getFavorite();
            FavoriteType type = f.getType();
            switch (type) {
                case Category:
                case UserResponses:
                case SubThread:
                    tasks.add(new FavoriteStatLoadTask(fd));
                    break;
                case Thread:
                    if (f.getItemId() == post.getThreadRootId()) {
                        tasks.add(new FavoriteAdjustUnreadTask(fd, read ? -1 : 1));
                    }
                    break;
                case UserPosts:
                    if (f.getItemId() == post.getUserId()) {
                        tasks.add(new FavoriteAdjustUnreadTask(fd, read ? -1 : 1));
                    }
                    break;
                default:
                    tasks.add(new FavoriteAdjustUnreadTask(fd, read ? -1 : 1));
                    break;
            }
            i++;
        }

        return tasks.toArray(new ALoadTask[tasks.size()]);
    }

    private static class Stat {
        private final FavoriteStatData newStatistic;
        private final String newName;

        private Stat(FavoriteStatData newStatistic, String newName) {
            this.newStatistic = newStatistic;
            this.newName = newName;
        }
    }

    private class FavoriteStatLoadTask extends ALoadTask<Stat> {
        private final FavoriteItem item;

        public FavoriteStatLoadTask(FavoriteItem item) {
            this.item = item;
        }

        @Override
        public Stat doBackground() throws Exception {
            Favorite f = item.getFavorite();
            FavoriteType type = f.getType();
            int itemId = f.getItemId();

            FavoriteStatData newStatistic = type.loadStatistic(itemId);
            String newName = f.isNameSet() ? null : type.loadName(itemId);

            return new Stat(newStatistic, newName);
        }

        @Override
        public void doSwing(Stat data) {
            item.setStatistic(data.newStatistic);
            if (data.newName != null) {
                // Update favorite name
                item.setName(data.newName);
            }

            modelControl.itemUpdated(item);
        }
    }

    /**
     * @author xBlackCat
     */
    private class FavoritesLoadTask extends ALoadTask<Collection<FavoriteItem>> {
        @Override
        public Collection<FavoriteItem> doBackground() throws Exception {
            Collection<Favorite> favorites = storage.getFavoriteAH().getFavorites();
            Collection<FavoriteItem> items = new ArrayList<>(favorites.size());

            for (Favorite f : favorites) {
                FavoriteType type = f.getType();
                int itemId = f.getItemId();

                FavoriteStatData newStatistic = type.loadStatistic(itemId);
                String name = type.loadName(itemId);

                items.add(new FavoriteItem(f.setName(name), newStatistic));
            }


            return items;
        }

        @Override
        public void doSwing(Collection<FavoriteItem> data) {
            modelControl.removeChildren(favorites);

            for (FavoriteItem f : data) {
                modelControl.addChild(favorites, f);
            }
        }
    }

    private class FavoriteAdjustUnreadTask extends AnAdjustUnreadTask<FavoriteItem> {
        public FavoriteAdjustUnreadTask(FavoriteItem item, int adjustDelta) {
            super(item, adjustDelta);
        }

        @Override
        public void doSwing(Void data) {
            FavoriteStatData stat = item.getStatistic();

            item.setStatistic(new FavoriteStatData(stat.getUnread() + adjustDelta, stat.getTotal()));

            modelControl.itemUpdated(item);
        }
    }
}
