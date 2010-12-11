package org.xblackcat.rojac.data.favorite;

/**
 * @author xBlackCat
 */

public abstract class AnItemFavorite extends AFavorite {
    protected final int itemId;

    protected AnItemFavorite(Integer id, String config) {
        this(id, Integer.parseInt(config));
    }

    public AnItemFavorite(Integer id, int itemId) {
        super(id);

        this.itemId = itemId;
    }

    @Override
    public String getConfig() {
        return String.valueOf(itemId);
    }
}
