package org.xblackcat.rojac.gui.view.recenttopics;

import gnu.trove.map.hash.TIntObjectHashMap;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.IMessageAH;
import org.xblackcat.rojac.service.storage.Storage;
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

        // TODO: optimize loading latest topics
        IMessageAH messageAH = Storage.get(IMessageAH.class);
        int[] latestTopics = messageAH.getLatestTopics(listSize);

        TIntObjectHashMap<Forum> forums = new TIntObjectHashMap<>();

        for (int lastPostId : latestTopics) {
            final MessageData lastPost = messageAH.getMessageData(lastPostId);

            if (lastPost == null) {
                // Post is not found ???
                continue;
            }

            Forum f = forums.get(lastPost.getForumId());
            if (f == null) {
                f = Storage.get(IForumAH.class).getForumById(lastPost.getForumId());
                forums.put(f.getForumId(), f);
            }

            // TODO: implement loading last post in thread.
            MessageData lastThread;
            if (lastPost.getTopicId() == 0) {
                // Message is topic
                lastThread = lastPost;
            } else {
                lastThread = messageAH.getMessageData(lastPost.getTopicId());
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
