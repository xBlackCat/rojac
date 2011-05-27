package org.xblackcat.rojac.gui.theme;

/**
 * Definition of available icons to be used in thread view for posts.
 *
 * @author xBlackCat
 */

public enum FavoritesIcon implements AnIcon {
    ThreadRead("favorite-thread-read.png"),
    ThreadReadPartially("favorite-thread-read-partially.png"),
    ThreadUnread("favorite-thread-unread.png"),
    UserPostsRead("favorite-userposts-read.png"),
    UserPostsReadPartially("favorite-userposts-read-partially.png"),
    UserPostsUnread("favorite-userposts-unread.png"),;

    private final String filename;

    private FavoritesIcon(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
