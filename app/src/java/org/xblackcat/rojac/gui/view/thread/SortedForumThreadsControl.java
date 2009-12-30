package org.xblackcat.rojac.gui.view.thread;

import gnu.trove.TIntHashSet;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.data.ThreadStatData;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.executor.IExecutor;
import org.xblackcat.rojac.service.storage.IMessageAH;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.StorageException;

import javax.swing.*;
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

        SwingWorker sw = new SwingWorker<Void, Thread>() {
            @Override
            protected Void doInBackground() throws Exception {
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

        executor.execute(sw);
        return forumId;
    }

    @Override
    public void updateItem(AThreadModel<Post> model, int... itemIds) {
        ForumRoot forumRoot = (ForumRoot) model.getRoot();

        TIntHashSet newPosts = new TIntHashSet();
        TIntHashSet toUpdate = new TIntHashSet();

        for (int messageId : itemIds) {
            if (forumRoot.containsId(messageId)) {
                toUpdate.add(messageId);
            } else {
                newPosts.add(messageId);
            }
        }

        // Store forumId
        final int forumId = forumRoot.getMessageData().getForumId();

        executor.execute(new MessagesLoader(forumRoot, model, newPosts.toArray()));
    }

    @Override
    public void loadChildren(final AThreadModel<Post> threadModel, Post p) {
        //  In the Sorted model only Thread object could be loaded.

        // Watch out for the line!
        final Thread item = (Thread) p;

        item.setLoadingState(LoadingState.Loading);

        executor.execute(new ThreadLoader(item, threadModel));
    }

    @Override
    public boolean isRootVisible() {
        return false;
    }

    private class MessagesLoader extends SwingWorker<Void, MessageData> {
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
        protected Void doInBackground() throws Exception {
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
                Post newPost = item.insertPost(data);
                Post parentPost = newPost.getParent();
                threadModel.nodeWasAdded(parentPost, newPost);
                threadModel.nodeStructureChanged(parentPost);
            }

            threadModel.nodeStructureChanged(item);
        }
    }

    private class ThreadLoader extends SwingWorker<Void, MessageData> {
        private final Thread item;
        private final AThreadModel<Post> threadModel;

        public ThreadLoader(Thread item, AThreadModel<Post> threadModel) {
            this.item = item;
            this.threadModel = threadModel;
        }

        @Override
        protected Void doInBackground() throws Exception {
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