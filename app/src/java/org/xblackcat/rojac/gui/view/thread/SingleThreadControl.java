package org.xblackcat.rojac.gui.view.thread;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.gui.view.MessageChecker;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.IStorage;

/**
 * Control class of all threads of the specified forum.
 *
 * @author xBlackCat
 */

public class SingleThreadControl implements IThreadControl<Post> {
    private static final Log log = LogFactory.getLog(SingleThreadControl.class);

    protected final IStorage storage = ServiceFactory.getInstance().getStorage();

    @Override
    public void fillModelByItemId(final AThreadModel<Post> model, AThreadView id, int threadId) {
        MessageData fakeMessageData = new MessageData(threadId, 0, 0, 0, 0, null, "Loading...", 0L, 0L, false, null);
        final Thread rootItem = new Thread(fakeMessageData, null);

        model.setRoot(rootItem);

        new MessageChecker(threadId) {
            @Override
            protected void done() {
                rootItem.setMessageData(data);
                model.nodeChanged(rootItem);

                loadThread(model, rootItem, null);
            }
        }.execute();
    }

    @Override
    public void markForumRead(AThreadModel<Post> model, boolean read) {
        // Root post is Thread object
        model.getRoot().setDeepRead(read);

        model.subTreeNodesChanged(model.getRoot());
    }

    @Override
    public void markThreadRead(AThreadModel<Post> model, int threadRootId, boolean read) {
        Post post = model.getRoot();
        if (post.getMessageId() == threadRootId) {
            post.setDeepRead(read);

            model.subTreeNodesChanged(post);
        }
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
        return true;
    }

    @Override
    public void updateModel(AThreadModel<Post> model, int[] threadIds) {
        Post root = model.getRoot();

        if (ArrayUtils.contains(threadIds, root.getMessageId())) {
            // Thread always filled in.
            loadThread(model, root, null);
        }
    }

    @Override
    public String getTitle(AThreadModel<Post> model) {
        // Root is Thread object
        Post root = model.getRoot();
        if (root.getMessageData() != null && root.getMessageData().getSubject() != null) {
            return root.getMessageData().getSubject();
        } else {
            return "#" + root.getMessageId();
        }
    }
}
