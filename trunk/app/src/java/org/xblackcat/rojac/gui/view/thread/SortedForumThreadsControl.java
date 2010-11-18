package org.xblackcat.rojac.gui.view.thread;

import gnu.trove.TIntHashSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.RojacDebugException;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.IStorage;

/**
 * Control class of all threads of the specified forum.
 *
 * @author xBlackCat
 */

public class SortedForumThreadsControl implements IThreadControl<Post> {
    private static final Log log = LogFactory.getLog(SortedForumThreadsControl.class);

    protected final IStorage storage = ServiceFactory.getInstance().getStorage();

    @Override
    public void fillModelByItemId(final AThreadModel<Post> model, int forumId) {
        final ForumRoot rootItem = new ForumRoot(forumId);

        model.setRoot(rootItem);

        new ThreadsLoader(forumId, rootItem, model).execute();
    }

    @Override
    public void markForumRead(AThreadModel<Post> model, boolean read) {
        // Root post is ForumRoot object
        model.getRoot().setRead(read);

        model.subTreeNodesChanged(model.getRoot());
    }

    @Override
    public void markThreadRead(AThreadModel<Post> model, int threadRootId, boolean read) {
        final Post post = model.getRoot().getMessageById(threadRootId);
        post.setDeepRead(read);

        model.subTreeNodesChanged(post);
    }

    @Override
    public void markPostRead(AThreadModel<Post> model, int postId, boolean read) {
        final Post post = model.getRoot().getMessageById(postId);
        if (post != null) {
            post.setRead(read);
            model.pathToNodeChanged(post);
        }
    }

    @Override
    public void loadThread(final AThreadModel<Post> threadModel, Post p, IItemProcessor<Post> postProcessor) {
        //  In the Sorted model only Thread object could be loaded.

        // Watch out for the line!
        final Thread item = (Thread) p;

        item.setLoadingState(LoadingState.Loading);

        new ThreadLoader(item, threadModel, postProcessor).execute();
    }

    @Override
    public boolean isRootVisible() {
        return false;
    }

    @Override
    public void updateModel(AThreadModel<Post> model, int[] threadIds) {
        TIntHashSet filledThreads = new TIntHashSet();
        for (Post post : model.getRoot().getChildren()) {
            if (post.getThreadRoot().isFilled()) {
                filledThreads.add(post.getThreadRoot().getMessageId());
            }
        }

        // Retain only changed filled thread to reload.
        filledThreads.retainAll(threadIds);

        // Reload filled threads.
        for (int threadId : filledThreads.toArray()) {
            Post post = model.getRoot().getMessageById(threadId);
            if (post != null) {
                // Update thread children
                Thread t = post.getThreadRoot();

                if (t.isFilled()) {
                    // Thread is already filled - update children
                    loadThread(model, t, null);
                } else {
                    throw new RojacDebugException("Expected filled thread #" + threadId);
                }
            } else {
                throw new RojacDebugException("Thread #" + threadId + " not found");
            }
        }

        // Reload forum threads list.
        new ThreadsLoader(model.getRoot().getForumId(), (ForumRoot) model.getRoot(), model).execute();
    }
}
