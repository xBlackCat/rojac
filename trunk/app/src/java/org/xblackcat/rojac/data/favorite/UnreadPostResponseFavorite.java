package org.xblackcat.rojac.data.favorite;

import java.util.Random;

/**
 * @author xBlackCat
 */

class UnreadPostResponseFavorite extends AFavorite {
    private final Random random = new Random();

    UnreadPostResponseFavorite(Integer id, String name, String config) {
        super(name, id);
    }

    @Override
    public void updateStatistic(Runnable runnable) {
    }

    @Override
    public FavoriteType getFavoriteType() {
        return FavoriteType.UnreadPostResponses;
    }

    @Override
    public boolean isHighlighted() {
        return random.nextBoolean();
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Override
    public String getStatistic() {
        Random random = this.random;
        int v = random.nextInt(100);
        int t = v + random.nextInt(100);
        return v + " of " + t;
    }

    @Override
    protected void setConfig(String configString) {

    }
}
