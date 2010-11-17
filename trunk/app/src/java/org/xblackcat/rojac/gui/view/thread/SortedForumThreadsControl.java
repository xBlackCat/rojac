package org.xblackcat.rojac.gui.view.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.data.ThreadStatData;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.IMessageAH;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.RojacWorker;

import java.util.List;

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

    protected void updateState(AThreadModel<Post> model, Post post) {
        model.pathToNodeChanged(post);
        for (Post p : post.getChildren()) {
            updateState(model, p);
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
    public void loadChildren(final AThreadModel<Post> threadModel, Post p, IItemProcessor<Post> postProcessor) {
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

    private class ThreadLoader extends RojacWorker<Void, MessageData> {
        private final Thread item;
        private final AThreadModel<Post> threadModel;
        private final IItemProcessor<Post> postProcessor;

        public ThreadLoader(Thread item, AThreadModel<Post> threadModel, IItemProcessor<Post> postProcessor) {
            this.item = item;
            this.threadModel = threadModel;
            this.postProcessor = postProcessor;
        }

        @Override
        protected Void perform() throws Exception {
            int itemId = item.getMessageId();
            int forumId = item.getForumId();

            MessageData[] messages;
            try {
                messages = storage.getMessageAH().getMessagesDataByTopicId(itemId, forumId);
            } catch (StorageException e) {
                log.error("Can not load message children for id = " + itemId + " (forum #" + forumId + ")", e);
                throw e;
            }

            publish(messages);

            return null;
        }

        @Override
        protected void process(List<MessageData> chunks) {
            item.fillThread(chunks);
            item.setLoadingState(LoadingState.Loaded);

            threadModel.nodeStructureChanged(item);

            if (postProcessor != null) {
                postProcessor.processItem(item);
            }
        }
    }

    private class ThreadsLoader extends RojacWorker<Void, Thread> {
        private final int forumId;
        private final ForumRoot rootItem;
        private final AThreadModel<Post> model;

        public ThreadsLoader(int forumId, ForumRoot rootItem, AThreadModel<Post> model) {
            this.forumId = forumId;
            this.rootItem = rootItem;
            this.model = model;
        }

        @Override
        protected Void perform() throws Exception {
            MessageData[] threadPosts;
            IMessageAH mAH = storage.getMessageAH();
            try {
                threadPosts = mAH.getTopicMessagesDataByForumId(forumId);
            } catch (StorageException e) {
                log.error("Can not load topics for forum #" + forumId, e);
                throw e;
            }

            for (MessageData threadPost : threadPosts) {
                int topicId = threadPost.getMessageId();

                try {
                    int unreadPosts = mAH.getUnreadReplaysInThread(topicId);
                    ThreadStatData stat = mAH.getThreadStatByThreadId(topicId);

                    publish(new Thread(threadPost, stat, unreadPosts, rootItem));
                } catch (StorageException e) {
                    log.error("Can not load statistic for topic #" + topicId, e);
                }
            }


            return null;
        }

        @Override
        protected void process(List<Thread> chunks) {
            rootItem.addThreads(chunks);

            model.nodeStructureChanged(rootItem);
        }

        @Override
        protected void done() {
            model.markInitialized();
        }
    }
}
