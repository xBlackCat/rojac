package org.xblackcat.rojac.gui.view.model;

import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.OpenMessageMethod;
import org.xblackcat.rojac.gui.popup.PopupMenuBuilder;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.util.RojacUtils;

import javax.swing.*;

/**
 * Handle common thread-model behaviour
 */
abstract class AThreadsModelControl implements IModelControl<Post> {
    @Override
    public Post getTreeRoot(Post post) {
        return post == null ? null : post.getThreadRoot();
    }

    @Override
    public JPopupMenu getItemMenu(Post post, IAppControl appControl) {
        return PopupMenuBuilder.getTreeViewMenu(post, appControl, true);
    }

    @Override
    public void resortModel(AThreadModel<Post> model) {
        model.root.deepResort();
        model.modelSupport.fireNewRoot();
    }

    @Override
    public boolean allowSearch() {
        return true;
    }

    protected void markThreadRead(AThreadModel<Post> model, int threadRootId, boolean read) {
        assert RojacUtils.checkThread(true);

        Post post = model.getRoot().getMessageById(threadRootId);
        if (post != null) {
            post.setDeepRead(read);

            model.pathToNodeChanged(post);
            model.subTreeNodesChanged(post);
        }
    }

    protected void markPostRead(AThreadModel<Post> model, int postId, boolean read) {
        assert RojacUtils.checkThread(true);

        final Post post = model.getRoot().getMessageById(postId);
        if (post != null) {
            post.setRead(read);
            model.pathToNodeChanged(post);
        }
    }

    @Override
    public void loadThread(final AThreadModel<Post> threadModel, Post p, Runnable postProcessor) {
        assert RojacUtils.checkThread(true);

        //  In the Sorted model only Thread object could be loaded.

        // Watch out for the line!
        final Thread item = (Thread) p;

        item.setLoadingState(LoadingState.Loading);

        new ThreadLoader(threadModel, item, postProcessor).execute();
    }

    @Override
    public void onDoubleClick(Post post, IAppControl appControl) {
        OpenMessageMethod openMethod = getOpenMessageMethod().get();
        if (openMethod != null) {
            appControl.openMessage(post.getMessageId(), openMethod);
        }
    }

    /**
     * Method to open a post by double-click
     *
     * @return
     */
    protected abstract Property<OpenMessageMethod> getOpenMessageMethod();
}
