package org.xblackcat.rojac.gui.view.favorites;

/**
 * @author xBlackCat
 */

public enum FavoriteType {
    UnreadUserPosts,
    UnreadThreadPosts,
    UnreadUserAnswers,
    Category;

    String getTypeName() {
        return "Type name:";
    }
}
