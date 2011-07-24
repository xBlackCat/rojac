package org.xblackcat.rojac.gui.view.favorites;

import org.xblackcat.rojac.data.Favorite;
import org.xblackcat.rojac.data.FavoriteStatData;

/**
 * @author xBlackCat Date: 24.07.11
 */
class FavoriteData {
    private Favorite favorite;
    private FavoriteStatData statistic;

    FavoriteData(Favorite favorite) {
        this.favorite = favorite;
    }

    public Favorite getFavorite() {
        return favorite;
    }

    public FavoriteStatData getStatistic() {
        return statistic;
    }

    public void setStatistic(FavoriteStatData statistic) {
        this.statistic = statistic;
    }

    public void setName(String newName) {
        favorite = favorite.setName(newName);
    }

    public boolean isExuded() {
        return statistic != null && statistic.getUnread() > 0;
    }
}
