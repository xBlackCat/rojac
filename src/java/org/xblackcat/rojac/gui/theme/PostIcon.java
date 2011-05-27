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
    OwnPostReadPartially("message-read-partially-my.png"),
    OwnPostRead("message-read-my.png"),
    HasResponseUnread("message-unread-has-response.png"),
    HasResponseReadPartially("message-read-partially-has-response.png"),
    HasResponseRead("message-read-has-response.png"),
    ResponseRead("message-read-response.png"),
    ResponseReadPartially("message-read-partially-response.png"),
    ResponseUnread("message-unread-response.png"),
    NewMessage("message-new.png");

    private final String filename;

    private PostIcon(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
