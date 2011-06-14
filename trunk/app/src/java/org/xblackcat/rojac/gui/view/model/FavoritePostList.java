package org.xblackcat.rojac.gui.view.model;

import org.xblackcat.rojac.data.IFavorite;

/**
 * Root node for
 *
 * @author xBlackCat
 */

class FavoritePostList extends PostList {
    // State fields
    private final IFavorite favorite;

    public FavoritePostList(IFavorite favorite) {
        this.favorite = favorite;
    }

    /**
     * Returns associated favorite with the list.
     *
     * @return associated favorite.
     */
    public IFavorite getFavorite() {
        return favorite;
    }
}
