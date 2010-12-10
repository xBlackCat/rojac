package org.xblackcat.rojac.data.favorite;

/**
 * @author xBlackCat
 */

abstract class AFavorite implements IFavorite {
    protected final String name;
    protected final Integer id;

    protected AFavorite(String name, Integer id) {
        this.name = name;
        this.id = id;
    }

    protected abstract void setConfig(String configString);

    @Override
    public String getName() {
        return name;
    }
}
