package org.xblackcat.rojac.gui.theme;

/**
 * Definition of available icons to be used in thread view for posts.
 *
 * @author xBlackCat
 */

// TODO: investigate, how to remove the last item from the enum. Is it possible to move it into ReadStatusIcon?
public enum ThreadIcon implements AnIcon {
    HasResponseUnread("thread-has-response.png");

    private final String filename;

    private ThreadIcon(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
