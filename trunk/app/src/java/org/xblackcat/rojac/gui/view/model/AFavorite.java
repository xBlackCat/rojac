package org.xblackcat.rojac.gui.view.model;

import org.xblackcat.rojac.data.FavoriteStatData;
import org.xblackcat.rojac.data.IFavorite;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.RojacUtils;
import org.xblackcat.rojac.util.RojacWorker;

import java.util.List;

/**
 * @author xBlackCat
 */

abstract class AFavorite implements IFavorite {
    protected final int itemId;

    protected FavoriteStatData statistic = null;
    protected final int id;
    private String name;
    protected final IStorage storage = ServiceFactory.getInstance().getStorage();

    public AFavorite(int id, int itemId) {
        this.id = id;

        this.itemId = itemId;
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
        new ValuesLoader(callback, AFavorite.this).execute();
    }

    protected abstract FavoriteStatData loadStatistic() throws StorageException;

    @Override
    public int getId() {
        return id;
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
        private final AFavorite favorite;

        public ValuesLoader(Runnable callback, AFavorite favorite) {
            this.callback = callback;
            this.favorite = favorite;
        }

        @Override
        protected Void perform() throws Exception {
            amount = loadStatistic();
            if (initName) {
                name = loadName();
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
