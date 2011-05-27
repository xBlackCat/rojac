package org.xblackcat.rojac.gui.view.recenttopics;

import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.MessageData;

/**
 * @author xBlackCat
 */

class LastPostInfo {
    private final Forum forum;
    private final MessageData topicRoot;
    private final MessageData lastPost;

    LastPostInfo(Forum forum, MessageData topicRoot, MessageData lastPost) {
        this.forum = forum;
        this.topicRoot = topicRoot;
        this.lastPost = lastPost;
    }

    public Forum getForum() {
        return forum;
    }

    public MessageData getTopicRoot() {
        return topicRoot;
    }

    public MessageData getLastPost() {
        return lastPost;
    }
}
