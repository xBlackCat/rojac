package org.xblackcat.rojac.gui.view.model;

import org.xblackcat.rojac.NotImplementedException;
import org.xblackcat.rojac.gui.view.thread.ThreadToolbarActions;
import org.xblackcat.rojac.util.RojacUtils;

/**
 * @author xBlackCat
 */

abstract class MessageListControl implements IModelControl {
    private static final ThreadToolbarActions[] TOOLBAR_CONFIG = new ThreadToolbarActions[]{
            ThreadToolbarActions.PreviousPost,
            ThreadToolbarActions.NextPost,
            ThreadToolbarActions.PreviousUnread,
            ThreadToolbarActions.NextUnread,
    };

    @Override
    public void loadThread(SortedThreadsModel model, Post item, Runnable postProcessor) {
        throw new NotImplementedException("The method shouldn't be used.");
    }

    @Override
    public boolean isRootVisible() {
        assert RojacUtils.checkThread(true);

        return false;
    }

    @Override
    public Post getTreeRoot(Post post) {
        // Post lists have no parents or children.
        return post;
    }

    @Override
    public boolean allowSearch() {
        return false;
    }

    @Override
    public void resortModel(SortedThreadsModel model) {
        // Nothing to do
    }

    @Override
    public ThreadToolbarActions[] getToolbar() {
        return TOOLBAR_CONFIG;
    }

    @Override
    public boolean isToolBarButtonVisible(ThreadToolbarActions action, Post post) {
        return true;
    }

    @Override
    public void unloadThread(SortedThreadsModel model, Post item) {
        // Nothing to do
    }


    protected void markPostRead(SortedThreadsModel model, int postId, boolean read) {
        assert RojacUtils.checkThread(true);

        Post p = model.getRoot().getMessageById(postId);

        if (p == null) {
            return;
        }

        boolean postRead = p.isRead() != ReadStatus.Unread;
        if (postRead != read) {
            p.setRead(read);
            model.nodeChanged(p);
        }
    }

    protected abstract void updateModel(SortedThreadsModel model, Runnable postProcessor);
}
