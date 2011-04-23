package org.xblackcat.rojac.gui.view.recenttopics;

import gnu.trove.TIntObjectHashMap;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.util.RojacWorker;

import java.util.Collection;
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
        final Integer listSize = Property.VIEW_LATEST_POSTS_SIZE.get();

        final IStorage storage = ServiceFactory.getInstance().getStorage();

        // TODO: optimize loading latest topics
        Collection<MessageData> latestTopics = storage.getMessageAH().getLatestTopics(listSize);

        TIntObjectHashMap<Forum> forums = new TIntObjectHashMap<Forum>();

        for (MessageData topic : latestTopics) {
            Forum f = forums.get(topic.getForumId());
            if (f == null) {
                f = storage.getForumAH().getForumById(topic.getForumId());
                forums.put(f.getForumId(), f);
            }

            // TODO: implement loading last post in thread.
            MessageData lastPost = topic;

            publish(new LastPostInfo(
                    f,
                    topic,
                    lastPost
            ));
        }

        return null;
    }

    @Override
    protected void process(List<LastPostInfo> chunks) {
        model.addLatestPosts(chunks);
    }
}
