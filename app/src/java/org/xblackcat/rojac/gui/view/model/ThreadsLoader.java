package org.xblackcat.rojac.gui.view.model;

import gnu.trove.list.array.TIntArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.data.ThreadStatData;
import org.xblackcat.rojac.service.storage.IMessageAH;
import org.xblackcat.rojac.service.storage.IStatisticAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.RojacUtils;
import org.xblackcat.rojac.util.RojacWorker;

import java.util.List;

/**
 * Worker to load collapsed thread - only thread info.
 *
 * @author xBlackCat
 */
class ThreadsLoader extends RojacWorker<Void, Thread> {
    private static final Log log = LogFactory.getLog(ThreadsLoader.class);

    private final int forumId;
    private final ForumRoot rootItem;

    public ThreadsLoader(final Runnable postProcessor, final AThreadModel<Post> model, int forumId) {
        super(new Runnable() {
            @Override
            public void run() {
                model.markInitialized();

                model.fireResortModel();

                if (postProcessor != null) {
                    postProcessor.run();
                }
            }
        });
        assert RojacUtils.checkThread(true);

        this.forumId = forumId;
        this.rootItem = (ForumRoot) model.getRoot();
    }

    @Override
    protected Void perform() throws Exception {
        IStatisticAH mAH = Storage.get(IStatisticAH.class);
        try {
            Iterable<MessageData> threadPosts = Storage.get(IMessageAH.class).getTopicMessagesDataByForumId(forumId);

            for (MessageData threadPost : threadPosts) {
                int topicId = threadPost.getMessageId();

                try {
                    int unreadPosts = mAH.getReplaysInThread(topicId).getUnread();
                    ThreadStatData stat = mAH.getThreadStatByThreadId(topicId);

                    publish(new Thread(threadPost, stat, unreadPosts, rootItem));
                } catch (StorageException e) {
                    log.error("Can not load statistic for topic #" + topicId, e);
                }
            }
        } catch (StorageException e) {
            log.error("Can not load topics for forum #" + forumId, e);
            throw e;
        }

        return null;
    }

    @Override
    protected void process(List<Thread> chunks) {
        TIntArrayList updatedNodes = new TIntArrayList();
//        List<Post> addedNodes = new LinkedList<Post>();
        List<Post> threads = rootItem.childrenPosts;

        for (Thread t : chunks) {
            if (threads.contains(t)) {
                int index = threads.indexOf(t);
                Thread realThread = (Thread) threads.get(index);
                if (!realThread.isFilled()) {
                    threads.set(index, t);
                }
                updatedNodes.add(index);
            } else {
                threads.add(t);
//                addedNodes.add(t);
            }
        }

//        model.nodesChanged(rootItem, updatedNodes.toNativeArray());
//        model.nodesAdded(rootItem, addedNodes.toArray(new Post[addedNodes.size()]));
    }
}
