package org.xblackcat.rojac.gui.view;

/**
 * @author xBlackCat
 */

public enum ViewType {
    Forum(new ForumThreadViewFactory()),
    SingleThread(new SingleThreadViewFactory()),
    SingleMessage(new MessageViewFactory()),
    Favorite(new FavoriteViewFactory()),
    PostList(new UserPostListFactory()),
    ReplyList(new UserReplyListFactory()),
    ;

    private final IViewFactory factory;

    private ViewType(IViewFactory factory) {
        this.factory = factory;
    }

    IViewFactory getFactory() {
        return factory;
    }

    public ViewId makeId(int itemId) {
        return new ViewId(itemId, this);
    }
}
