package org.xblackcat.rojac.gui.theme;

/**
 * Definition of available icons to be used in thread view for posts.
 *
 * @author xBlackCat
 */

public enum ViewIcon implements AnIcon {
    RecentTopics,
    ForumList,
    Favorites;

    private final String filename;

    private ViewIcon() {
        this.filename = "view-" + name().toLowerCase() + ".png";
    }

    public String getFilename() {
        return filename;
    }
}
