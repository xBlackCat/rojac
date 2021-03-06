package org.xblackcat.rojac.gui.view.model;

/**
 * Model control factory.
 *
 * @author xBlackCat
 */
public enum ModelControl {
    /**
     * Produces a model control to handle single thread in threads view.
     */
    SingleThread(new SingleThreadModelControl()),
    /**
     * Produces a model control to handle forum threads in threads view
     */
    ForumThreads(new SortedForumModelControl()),
    /**
     * Produces a model control to handle favorite item.
     */
    Favorites(null) {
        @Override
        public IModelControl get() {
            // The control is mutable - always generate a new instance.
            return new FavoritesModelControl();
        }
    },
    /**
     * Produces a model control to handle user's post list.
     */
    UserPosts(new PostListControl(false)),
    /**
     * Produces a model control to handle list of replies to user's posts.
     */
    UserReplies(new PostListControl(true)),
    /**
     * Produces a user messages in outbox
     */
    Outbox(new OutboxListControl()),
    IgnoredThreadList(new IgnoredThreadListControl()),
    ;

    private final IModelControl control;

    ModelControl(IModelControl control) {
        this.control = control;
    }

    public IModelControl get() {
        return control;
    }

}
