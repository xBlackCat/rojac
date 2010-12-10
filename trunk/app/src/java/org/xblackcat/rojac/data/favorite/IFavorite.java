package org.xblackcat.rojac.data.favorite;

/**
 * Base interface for hold information about favorite. It contains own renderer.
 *
 * @author xBlackCat
 */

public interface IFavorite {
    void updateStatistic(Runnable runnable);

    FavoriteType getFavoriteType();

    /**
     * Returns a favorite-specific config to store in database.
     *
     * @return parsable config string.
     */
    String getConfig();

    boolean isHighlighted();

    String getName();

    String getStatistic();
}
