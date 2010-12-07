package org.xblackcat.rojac.gui.view.favorites;

import java.util.Random;

/**
 * @author xBlackCat
 */

public class UnreadTrackingFavorite implements IFavorite {
    private final Random random = new Random();

    @Override
    public void updateStatistic(Runnable runnable) {
    }

    @Override
    public FavoriteType getFavoriteType() {
        return FavoriteType.UnreadThreadPosts;
    }

    @Override
    public boolean isMarked() {
        return random.nextBoolean();
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Override
    public String getName() {
        return "Test favorite";
    }

    @Override
    public String getStatistic() {
        Random random = this.random;
        int v = random.nextInt(100);
        int t = v + random.nextInt(100);
        return v + " of " + t;
    }
}
