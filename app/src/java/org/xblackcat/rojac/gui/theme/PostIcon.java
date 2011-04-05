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
    OwnPostUnread("message-unread-my.png"),
    OwnPost("message-read-my.png"),
    HasResponse("message-read-has-response.png"),
    HasResponseUnread("message-unread-has-response.png"),
    Response("message-read-response.png"),
    UnreadResponse("message-unread-response.png"),
    NewMessage("message-new.png");

    private final String filename;

    private PostIcon(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
