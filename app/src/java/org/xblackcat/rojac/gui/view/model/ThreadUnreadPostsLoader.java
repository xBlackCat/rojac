package org.xblackcat.rojac.gui.view.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.service.storage.IStatisticAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.RojacWorker;

import java.util.List;

/**
 * Worker to load collapsed thread - update only unread posts amount.
 *
 * @author xBlackCat
 */
class ThreadUnreadPostsLoader extends RojacWorker<Void, Integer> {
    private static final Log log = LogFactory.getLog(ThreadUnreadPostsLoader.class);

    private final Thread topic;
    private final AThreadModel<Post> model;

    public ThreadUnreadPostsLoader(Thread topic, AThreadModel<Post> model) {
        this.topic = topic;
        this.model = model;
    }

    @Override
    protected Void perform() throws Exception {
        IStatisticAH mAH = Storage.get(IStatisticAH.class);
        try {
            int unreadPosts = mAH.getReplaysInThread(topic.getMessageId()).getUnread();

            publish(unreadPosts);
        } catch (StorageException e) {
            log.error("Can not load statistic for topic #" + topic, e);
        }

        return null;
    }

    @Override
    protected void process(List<Integer> chunks) {
        for (int unreadPosts : chunks) {
            topic.setUnreadPosts(unreadPosts);
            model.pathToNodeChanged(topic);
        }
    }
}
