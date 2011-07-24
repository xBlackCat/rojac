package org.xblackcat.rojac.data;

import org.xblackcat.rojac.gui.view.model.FavoriteType;

/**
 * Base interface for hold information about favorite. It contains own renderer.
 *
 * @author xBlackCat
 */

public interface IFavorite {
    void updateStatistic(Runnable runnable);

    FavoriteType getType();

    boolean isExuded();

    String getName();

    int getId();

    FavoriteStatData getStatistic();

    int getItemId();
}
