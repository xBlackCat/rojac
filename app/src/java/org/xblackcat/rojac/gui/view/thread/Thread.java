package org.xblackcat.rojac.gui.view.thread;

import gnu.trove.TIntObjectHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.MessageData;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author xBlackCat
 */

public class Thread extends Post {
    private static final Log log = LogFactory.getLog(Thread.class);

    private TIntObjectHashMap<Post> threadPosts;
    private final static Comparator<MessageData> SORT_BY_PARENTS = new Comparator<MessageData>() {
        @Override
        public int compare(MessageData o1, MessageData o2) {
            return o1.getParentId() - o2.getParentId();
        }
    };

    private final Post loadPostItem;

    // State fields
    private ReadStatus readStatus;
    private boolean filled = false;
    private LoadingState loadingState = LoadingState.NotLoaded;
    private boolean empty;

    public Thread(MessageData messageData, Post parent, boolean empty, ReadStatus read) {
        super(messageData, parent, null, read == ReadStatus.Read);

        threadPosts.put(messageData.getMessageId(), this);

        readStatus = read;
        loadPostItem = new Post(constructDummyMessageData(messageData), this, this);

        this.empty = empty;
    }

    protected Post getThreadRoot() {
        return this;
    }

    @Override
    public int getIndex(Post p) {
        if (!filled) {
            if (empty || p != loadPostItem) {
                throw new UnsupportedOperationException("Can not get index on uninitialized collection of post " + p);
            } else {
                return 0;
            }
        }
        return super.getIndex(p);
    }

    @Override
    public Post getChild(int idx) {
        if (filled) {
            return super.getChild(idx);
        } else if (empty) {
            throw new IndexOutOfBoundsException("There is no responses on post " + messageData);
        } else {
            return loadPostItem;
        }
    }

    @Override
    public int getSize() {
        if (filled) {
            return super.getSize();
        } else {
            return empty ? 0 : 1;
        }
    }

    @Override
    public LoadingState getLoadingState() {
        return empty ? LoadingState.Loaded : loadingState;
    }

    private void storePosts(MessageData... posts) {
        Arrays.sort(posts, SORT_BY_PARENTS);

        for (MessageData post : posts) {
            Post parent = threadPosts.get(post.getParentId());

            if (parent == null) {
                if (log.isDebugEnabled()) {
                    log.debug("There is no parent post exists for post " + post);
                }
                continue;
            }

            // TODO: compute the flag depending on MessageData and user settings.
            boolean read = false;

            Post p = new Post(post, parent, this, read);
            threadPosts.put(post.getMessageId(), p);
        }
    }

    private static MessageData constructDummyMessageData(MessageData messageData) {
        return new MessageData(-1, messageData.getMessageId(), messageData.getMessageId(), messageData.getForumId(), -1, null, null, -1, -1);
    }
}
