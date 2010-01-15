package org.xblackcat.rojac.gui.view.thread;

import gnu.trove.TIntHashSet;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.data.ThreadStatData;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.executor.IExecutor;
import org.xblackcat.rojac.service.executor.TaskType;
import org.xblackcat.rojac.service.storage.IMessageAH;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.RojacWorker;

import java.util.Arrays;
import java.util.List;

/**
 * Control class of all threads of the specified forum.
 *
 * @author xBlackCat
 */

public class SortedForumThreadsControl implements IThreadControl<Post> {
    private static final Log log = LogFactory.getLog(SortedForumThreadsControl.class);

    protected final IStorage storage = ServiceFactory.getInstance().getStorage();
    protected final IExecutor executor = ServiceFactory.getInstance().getExecutor();

    @Override
    public int loadThreadByItem(final AThreadModel<Post> model, final int forumId) {
        final ForumRoot rootItem = new ForumRoot(forumId);

        model.setRoot(rootItem);

        RojacWorker sw = new RojacWorker<Void, Thread>() {
            @Override
            protected Void perform() throws Exception {
                MessageData[] threadPosts;
                IMessageAH mAH = storage.getMessageAH();
                try {
                    threadPosts = mAH.getTopicMessageDatasByForumId(forumId);
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
        };

        executor.execute(sw, TaskType.MessageLoading);
        return forumId;
    }

    @Override
    public void updateItem(AThreadModel<Post> model, int... itemIds) {
        ForumRoot forumRoot = (ForumRoot) model.getRoot();

        TIntHashSet newPosts = new TIntHashSet();

        // Update existing nodes first.
        for (int messageId : itemIds) {
            Post post = forumRoot.getMessageById(messageId);
            if (post != null) {
                model.pathToNodeChanged(post);
            } else {
                newPosts.add(messageId);
            }
        }

        if (newPosts.isEmpty()) {
            // Nothing to load.
            return;
        }

        executor.execute(new MessagesLoader(forumRoot, model, newPosts.toArray()), TaskType.MessageLoading);
    }

    @Override
    public void loadChildren(final AThreadModel<Post> threadModel, Post p) {
        //  In the Sorted model only Thread object could be loaded.

        // Watch out for the line!
        final Thread item = (Thread) p;

        item.setLoadingState(LoadingState.Loading);

        executor.execute(new ThreadLoader(item, threadModel), TaskType.MessageLoading);
    }

    @Override
    public boolean isRootVisible() {
        return false;
    }

    private class MessagesLoader extends RojacWorker<Void, MessageData> {
        private final ForumRoot item;
        private final AThreadModel<Post> threadModel;
        private int[] messageIds;

        public MessagesLoader(ForumRoot item, AThreadModel<Post> threadModel, int[] messageIds) {
            this.item = item;
            this.threadModel = threadModel;
            this.messageIds = messageIds;

            Arrays.sort(messageIds);

            if (log.isDebugEnabled()) {
                log.debug("Message ids to load: " + ArrayUtils.toString(messageIds));
            }
        }

        @Override
        protected Void perform() throws Exception {
            for (int itemId : messageIds) {
                try {
                    MessageData messageData = storage.getMessageAH().getMessageData(itemId);

                    if (messageData == null) {
                        // Message id was given from rating record.
                        continue;
                    }

                    // Check if the message from the forum
                    if (messageData.getForumId() == item.getMessageData().getForumId()) {
                        publish(messageData);
                    }
                } catch (Exception e) {
                    log.error("Can not load message children for id = " + itemId, e);
                    throw e;
                }
            }

            return null;
        }

        @Override
        protected void process(List<MessageData> chunks) {
            for (MessageData data : chunks) {
                item.insertPost(data);
            }

            threadModel.nodeStructureChanged(item);
        }
    }

    private class ThreadLoader extends RojacWorker<Void, MessageData> {
        private final Thread item;
        private final AThreadModel<Post> threadModel;

        public ThreadLoader(Thread item, AThreadModel<Post> threadModel) {
            this.item = item;
            this.threadModel = threadModel;
        }

        @Override
        protected Void perform() throws Exception {
            int itemId = item.getMessageId();

            MessageData[] messages;
            try {
                messages = storage.getMessageAH().getMessageDatasByTopicId(itemId);
            } catch (StorageException e) {
                log.error("Can not load message children for id = " + itemId, e);
                throw e;
            }

            publish(messages);

            return null;
        }

        @Override
        protected void process(List<MessageData> chunks) {
            item.storePosts(chunks.toArray(new MessageData[chunks.size()]));
            item.setLoadingState(LoadingState.Loaded);

            threadModel.nodeStructureChanged(item);
        }
    }
}
