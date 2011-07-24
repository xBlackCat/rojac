package org.xblackcat.rojac.gui.view.model;

import org.xblackcat.rojac.data.FavoriteStatData;
import org.xblackcat.rojac.data.IFavorite;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.util.RojacUtils;
import org.xblackcat.rojac.util.RojacWorker;

import java.util.List;

/**
 * @author xBlackCat
 */

public class Favorite implements IFavorite {
    protected final IStorage storage = ServiceFactory.getInstance().getStorage();

    protected final int itemId;
    protected final int id;
    protected final FavoriteType type;
    protected String name;

    protected FavoriteStatData statistic = null;

    public Favorite(int id, int itemId, FavoriteType type) {
        this.id = id;
        this.itemId = itemId;
        this.type = type;
    }

    @Override
    public int getItemId() {
        return itemId;
    }

    @Override
    public boolean isExuded() {
        return statistic != null && statistic.getUnread() > 0;
    }

    @Override
    public FavoriteStatData getStatistic() {
        return statistic;
    }

    @Override
    public void updateStatistic(Runnable callback) {
        new ValuesLoader(callback, Favorite.this).execute();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public FavoriteType getType() {
        return type;
    }

    @Override
    public String getName() {
        assert RojacUtils.checkThread(true);

        if (name == null) {
            return getType().getTypeName("#" + id);
        } else {
            return name;
        }
    }

    protected final boolean isNameDefault() {
        return name == null;
    }

    protected void setName(String name) {
        assert RojacUtils.checkThread(true);
        this.name = name;
    }

    private class ValuesLoader extends RojacWorker<Void, Void> {
        private FavoriteStatData amount;
        private String name;
        private final Runnable callback;
        private final boolean initName = isNameDefault();
        private final Favorite favorite;

        public ValuesLoader(Runnable callback, Favorite favorite) {
            this.callback = callback;
            this.favorite = favorite;
        }

        @Override
        protected Void perform() throws Exception {
            amount = getType().loadStatistic(itemId);
            if (initName) {
                name = favorite.getType().loadName(itemId);
            }

            publish();
            return null;
        }

        @Override
        protected void process(List<Void> chunks) {
            favorite.statistic = amount;
            if (initName) {
                favorite.setName(name);
            }
        }

        @Override
        protected void done() {
            callback.run();
        }
    }
}
