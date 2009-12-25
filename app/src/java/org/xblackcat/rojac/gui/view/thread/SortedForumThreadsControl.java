package org.xblackcat.rojac.gui.view.thread;

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
    public int loadThreadByItem(final AThreadModel<Post> model, final int threadId) {
        final ForumRoot rootItem = new ForumRoot(threadId);

        model.setRoot(rootItem);

        SwingWorker sw = new SwingWorker<Void, Thread>() {
            @Override
            protected Void doInBackground() throws Exception {
                MessageData[] threadPosts;
                IMessageAH mAH = storage.getMessageAH();
                try {
                    threadPosts = mAH.getMessageDatasByThreadId(threadId);
                } catch (StorageException e) {
                    log.error("Can not load topics for forum with id = " + threadId, e);
                    throw e;
                }

                for (MessageData threadPost : threadPosts) {
                    int unreadPosts = mAH.getUnreadReplaysInThread(threadId);
                    ThreadStatData stat = mAH.getThreadStatByThreadId(threadId);
                    
                    publish(new Thread(threadPost, stat, unreadPosts, rootItem));
                }


                return null;
            }

            @Override
            protected void process(List<Thread> chunks) {
                rootItem.setupThreads(chunks);

                model.nodeStructureChanged(rootItem);
            }
        };

        executor.execute(sw);
        return threadId;
    }

    @Override
    public void updateItem(AThreadModel<Post> model, int... itemId) {
    }

    @Override
    public void loadChildren(final AThreadModel<Post> threadModel, Post p) {
        //  In the Sorted model only Thread object could be loaded.

        // Watch out for the line!
        final Thread item = (Thread) p;
        
        item.setLoadingState(LoadingState.Loading);
        final int itemId = item.getMessageId();

        executor.execute(new SwingWorker<Void, MessageData>() {
            @Override
            protected Void doInBackground() throws Exception {
                MessageData[] messages;
                try {
                    messages = storage.getMessageAH().getMessageDatasByThreadId(itemId);
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
        });
    }

    @Override
    public boolean isRootVisible() {
        return false;
    }
}