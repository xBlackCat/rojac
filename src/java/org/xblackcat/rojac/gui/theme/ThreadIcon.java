package org.xblackcat.rojac.gui.theme;

/**
 * Definition of available icons to be used in thread view for posts.
 *
 * @author xBlackCat
 */

public enum ThreadIcon implements AnIcon {
    Read("thread-read.png"),
    ReadPartially("thread-read-partially.png"),
    Unread("thread-unread.png"),
    HasResponseUnread("thread-has-response.png");

    private final String filename;

    private ThreadIcon(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
