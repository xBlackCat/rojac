package org.xblackcat.rojac.gui.view.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.data.ThreadStatData;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.IMessageAH;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.RojacWorker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
* @author xBlackCat
*/
class ThreadsLoader extends RojacWorker<Void, Thread> {
    private static final Log log = LogFactory.getLog(ThreadsLoader.class);

    protected final IStorage storage = ServiceFactory.getInstance().getStorage();

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
        IMessageAH mAH = storage.getMessageAH();
        try {
            Collection<MessageData> threadPosts = mAH.getTopicMessagesDataByForumId(forumId);

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
        for (Thread t : chunks) {
            rootItem.addThread(t);
        }
    }

    @Override
    protected void done() {
        // Get list of changed nodes.
        updateNodesList(rootItem);

        model.markInitialized();
    }

    protected void updateNodesList(Post root) {
        List<Post> added = new ArrayList<Post>();

        List<Post> toCheck = new ArrayList<Post>();

        for (Post p : root.getChildren()) {
            if (p.isNewNode()) {
                added.add(p);
                p.resetNewFlag();
            } else {
                toCheck.add(p);
            }
        }

        model.nodesAdded(rootItem, added.toArray(new Post[added.size()]));

        for (Post p : toCheck) {
            updateNodesList(p);
        }
    }
}
