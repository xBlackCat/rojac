package org.xblackcat.rojac.gui.view;

/**
 * @author xBlackCat
 */

public enum ViewType {
    Forum,
    SingleThread,
    SingleMessage,
    Favorite,
    PostList,
    ReplyList,
    ;

    private ViewType() {
    }

    public ViewId makeId(int itemId) {
        return new ViewId(itemId, this);
    }
}
