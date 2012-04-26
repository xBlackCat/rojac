package org.xblackcat.rojac.gui.view.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.ItemStatisticData;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IMessageAH;
import org.xblackcat.rojac.service.storage.IResult;
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

    protected final int forumId;
    protected final ForumRoot rootItem;
    protected final SortedThreadsModel model;

    public ThreadsLoader(final Runnable postProcessor, final SortedThreadsModel model, int forumId) {
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
        this.model = model;
        assert RojacUtils.checkThread(true);

        this.forumId = forumId;
        this.rootItem = (ForumRoot) model.getRoot();
    }

    @Override
    protected Void perform() throws Exception {
        boolean hideIgnored = Property.HIDE_IGNORED_TOPICS.get();
        boolean hideRead = Property.VIEW_THREAD_HIDE_READ_THREADS.get();

        try {
            int userId = Property.RSDN_USER_ID.get();
            try (IResult<ItemStatisticData<MessageData>> threadPosts = Storage.get(IMessageAH.class).getTopicMessagesDataByForumId(forumId, userId)) {
                for (ItemStatisticData<MessageData> threadPost : threadPosts) {
                    if (hideIgnored && threadPost.getItem().isIgnored()) {
                        continue;
                    }

                    if (hideRead && threadPost.getItemReadStatistic().getUnreadMessages() == 0) {
                        continue;
                    }

                    publish(new Thread(threadPost.getItem(), rootItem, threadPost.getItemReadStatistic()));
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
//        TIntArrayList updatedNodes = new TIntArrayList();
//        List<Post> addedNodes = new LinkedList<Post>();
        List<Post> threads = rootItem.childrenPosts;

        for (Thread t : chunks) {
            if (threads.contains(t)) {
                int index = threads.indexOf(t);
                Thread realThread = (Thread) threads.get(index);
                if (!realThread.isFilled()) {
                    threads.set(index, t);
                }
//                updatedNodes.add(index);
            } else {
                threads.add(t);
//                addedNodes.add(t);
            }
        }

//        model.nodesChanged(rootItem, updatedNodes.toNativeArray());
//        model.nodesAdded(rootItem, addedNodes.toArray(new Post[addedNodes.size()]));
    }
}
