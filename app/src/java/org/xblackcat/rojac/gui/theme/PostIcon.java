package org.xblackcat.rojac.gui.theme;

/**
 * Definition of available icons to be used in thread view for posts.
 *
 * @author xBlackCat
 */

public enum PostIcon implements AnIcon {
    Read("message-read.png"),
    ReadPartially("message-read-partially.png"),
    Unread("message-unread.png"),
    OwnPost("message-my.png"),
    HasResponse("message-has-response.png");

    private final String filename;

    private PostIcon(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
