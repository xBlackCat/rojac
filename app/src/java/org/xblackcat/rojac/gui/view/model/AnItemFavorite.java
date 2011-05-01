package org.xblackcat.rojac.gui.view.model;

import org.xblackcat.rojac.data.FavoriteStatData;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.RojacWorker;

import java.util.List;

/**
 * @author xBlackCat
 */

abstract class AnItemFavorite extends AFavorite {
    protected final int itemId;

    protected FavoriteStatData statistic = null;

    protected AnItemFavorite(int id, String config) {
        this(id, Integer.parseInt(config));
    }

    public AnItemFavorite(int id, int itemId) {
        super(id);

        this.itemId = itemId;
    }

    @Override
    public boolean isHighlighted() {
        return statistic != null && statistic.getUnread() > 0;
    }

    @Override
    public String getStatistic() {
        if (statistic != null) {
            return statistic.asString();
        } else {
            return "...";
        }
    }

    @Override
    public void updateStatistic(Runnable callback) {
        new ValuesLoader(callback).execute();
    }

    protected abstract FavoriteStatData loadStatistic() throws StorageException;

    private class ValuesLoader extends RojacWorker<Void, Void> {
        private FavoriteStatData amount;
        private String name;
        private final Runnable callback;
        private final boolean initName = isNameDefault();

        public ValuesLoader(Runnable callback) {
            this.callback = callback;
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
            AnItemFavorite.this.statistic = amount;
            if (initName) {
                AnItemFavorite.this.setName(name);
            }
        }

        @Override
        protected void done() {
            callback.run();
        }
    }
}
