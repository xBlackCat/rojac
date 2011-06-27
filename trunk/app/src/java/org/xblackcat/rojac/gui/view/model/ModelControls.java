package org.xblackcat.rojac.gui.view.model;

/**
 * @author xBlackCat
 */
public class ModelControls {
    public static final IModelControl<Post> SINGLE_THREAD = new SingleModelControl();
    public static final IModelControl<Post> FORUM_THREADS = new SortedForumModelControl();
    public static final IModelControl<Post> FAVORITES = new FavoritesModelControl();
    public static final IModelControl<Post> FAVORITES_MESSAGES = new FavoritesMessageListControl();
    public static final IModelControl<Post> USER_POSTS = new PostListControl(false);
    public static final IModelControl<Post> USER_REPLIES = new PostListControl(true);
}
