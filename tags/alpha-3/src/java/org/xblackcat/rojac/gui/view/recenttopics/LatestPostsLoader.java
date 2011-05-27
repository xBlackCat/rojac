package org.xblackcat.rojac.gui.view.recenttopics;

import gnu.trove.map.hash.TIntObjectHashMap;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.util.RojacWorker;

import java.util.List;

/**
 * @author xBlackCat
 */

class LatestPostsLoader extends RojacWorker<Void, LastPostInfo> {
    private final RecentThreadsModel model;

    public LatestPostsLoader(RecentThreadsModel model) {
        this.model = model;
    }

    @Override
    protected Void perform() throws Exception {
        final Integer listSize = Property.VIEW_RECENT_TOPIC_LIST_SIZE.get();

        final IStorage storage = ServiceFactory.getInstance().getStorage();

        // TODO: optimize loading latest topics
        int[] latestTopics = storage.getMessageAH().getLatestTopics(listSize);

        TIntObjectHashMap<Forum> forums = new TIntObjectHashMap<Forum>();

        for (int lastPostId : latestTopics) {
            final MessageData lastPost = storage.getMessageAH().getMessageData(lastPostId);

            Forum f = forums.get(lastPost.getForumId());
            if (f == null) {
                f = storage.getForumAH().getForumById(lastPost.getForumId());
                forums.put(f.getForumId(), f);
            }

            // TODO: implement loading last post in thread.
            MessageData lastThread;
            if (lastPost.getTopicId() == 0) {
                // Message is topic
                lastThread = lastPost;
            } else {
                lastThread = storage.getMessageAH().getMessageData(lastPost.getTopicId());
            }

            publish(new LastPostInfo(
                    f,
                    lastThread,
                    lastPost
            ));
        }

        return null;
    }

    @Override
    protected void process(List<LastPostInfo> chunks) {
        model.addLatestPosts(chunks);
    }

    @Override
    protected void done() {
        model.markAsLoaded();
    }
}
