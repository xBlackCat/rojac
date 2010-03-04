package org.xblackcat.rojac.gui.view.thread;

import gnu.trove.TIntHashSet;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.data.ThreadStatData;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.executor.IExecutor;
import org.xblackcat.rojac.service.janus.commands.AffectedMessage;
import org.xblackcat.rojac.service.storage.IMessageAH;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.RojacWorker;

import java.util.ArrayList;
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
    public int loadThreadByItem(final AThreadModel<Post> model, final AffectedMessage forum) {
        final int forumId = forum.getForumId();
        final ForumRoot rootItem = new ForumRoot(forumId);

        model.setRoot(rootItem);

        RojacWorker sw = new RojacWorker<Void, Thread>() {
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

                List<Thread> threads = new ArrayList<Thread>(threadPosts.length);
                for (MessageData threadPost : threadPosts) {
                    int topicId = threadPost.getMessageId();

                    try {
                        int unreadPosts = mAH.getUnreadReplaysInThread(topicId);
                        ThreadStatData stat = mAH.getThreadStatByThreadId(topicId);

                        threads.add(new Thread(threadPost, stat, unreadPosts, rootItem));
                    } catch (StorageException e) {
                        log.error("Can not load statistic for topic #" + topicId, e);
                    }
                }

                publish(threads.toArray(new Thread[threads.size()]));

                return null;
            }

            @Override
            protected void process(List<Thread> chunks) {
                rootItem.addThreads(chunks);

                model.nodeStructureChanged(rootItem);
            }
        };

        executor.execute(sw);
        return forumId;
    }

    @Override
    public void updateItem(AThreadModel<Post> model, AffectedMessage... itemIds) {
        ForumRoot forumRoot = (ForumRoot) model.getRoot();

        TIntHashSet newPosts = new TIntHashSet();

        // Update existing nodes first.
        for (AffectedMessage message : itemIds) {
            Post post = forumRoot.getMessageById(message.getMessageId());
            if (post != null) {
                model.pathToNodeChanged(post);
            } else {
                newPosts.add(message.getMessageId());
            }
        }

        if (newPosts.isEmpty()) {
            // Nothing to load.
            return;
        }

        executor.execute(new MessagesLoader(forumRoot, model, newPosts.toArray()));
    }

    @Override
    public void markForumRead(AThreadModel<Post> model, boolean read) {
        model.getRoot().setRead(read);

        model.subTreeNodesChanged(model.getRoot());
    }

    @Override
    public void markThreadRead(AThreadModel<Post> model, int threadRootId, boolean read) {
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

        executor.execute(new ThreadLoader(item, threadModel, postProcessor));
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
        private final IItemProcessor<Post> postProcessor;

        public ThreadLoader(Thread item, AThreadModel<Post> threadModel) {
            this(item, threadModel, null);
        }

        public ThreadLoader(Thread item, AThreadModel<Post> threadModel, IItemProcessor<Post> postProcessor) {
            this.item = item;
            this.threadModel = threadModel;
            this.postProcessor = postProcessor;
        }

        @Override
        protected Void perform() throws Exception {
            int itemId = item.getMessageId();

            MessageData[] messages;
            try {
                messages = storage.getMessageAH().getMessagesDataByTopicId(itemId);
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

            if (postProcessor!= null) {
                postProcessor.processItem(item);
            }
        }
    }
}
