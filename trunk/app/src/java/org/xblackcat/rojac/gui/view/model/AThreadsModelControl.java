package org.xblackcat.rojac.gui.view.model;

import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.popup.PopupMenuBuilder;
import org.xblackcat.rojac.gui.view.thread.IItemProcessor;
import org.xblackcat.rojac.util.RojacUtils;

import javax.swing.*;

/**
 * Handle common thread-model behaviour
 */
public abstract class AThreadsModelControl implements IModelControl<Post> {
    @Override
    public Post getTreeRoot(Post post) {
        return post == null ? null : post.getThreadRoot();
    }

    @Override
    public JPopupMenu getItemMenu(Post post, IAppControl appControl) {
        return PopupMenuBuilder.getTreeViewMenu(post, appControl, true, true);
    }

    @Override
    public void resortModel(AThreadModel<Post> model) {
        model.root.deepResort();
        model.reload();
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
    public void loadThread(final AThreadModel<Post> threadModel, Post p, IItemProcessor<Post> postProcessor) {
        assert RojacUtils.checkThread(true);

        //  In the Sorted model only Thread object could be loaded.

        // Watch out for the line!
        final Thread item = (Thread) p;

        item.setLoadingState(LoadingState.Loading);

        new ThreadLoader(item, threadModel, postProcessor).execute();
    }
}
