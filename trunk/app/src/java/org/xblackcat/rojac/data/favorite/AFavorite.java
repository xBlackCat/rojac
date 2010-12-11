package org.xblackcat.rojac.data.favorite;

/**
 * @author xBlackCat
 */

abstract class AFavorite implements IFavorite {
    protected final Integer id;

    protected AFavorite(Integer id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return getClass().getName();
    }
}
