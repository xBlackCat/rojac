package org.xblackcat.rojac.gui.view.thread;

import gnu.trove.TIntObjectHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.data.ThreadStatData;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author xBlackCat
 */

public class Thread extends Post {
    private static final Log log = LogFactory.getLog(Thread.class);

    private TIntObjectHashMap<Post> threadPosts = new TIntObjectHashMap<Post>();
    private final static Comparator<MessageData> SORT_BY_PARENTS = new Comparator<MessageData>() {
        @Override
        public int compare(MessageData o1, MessageData o2) {
            return o1.getParentId() - o2.getParentId();
        }
    };

    // State fields
    private ReadStatus readStatus;
    private boolean filled = false;
    private LoadingState loadingState = LoadingState.NotLoaded;
    private boolean empty;
    private ThreadStatData threadStatData;

    public Thread(MessageData messageData, ThreadStatData threadStatData, int unreadPosts, Post parent) {
        super(messageData, parent, null);
        this.threadStatData = threadStatData;

        threadPosts.put(messageData.getMessageId(), this);

        if (messageData.isRead()) {
            if (unreadPosts > 0) {
                readStatus = ReadStatus.ReadPartially;
            } else {
                readStatus = ReadStatus.Read;
            }
        } else {
            readStatus = ReadStatus.Unread;
        }

        this.empty = threadStatData.getReplyAmount() == 0;
    }

    protected Thread getThreadRoot() {
        return this;
    }

    @Override
    public int getIndex(Post p) {
        if (!filled) {
            return -1;
        }
        return super.getIndex(p);
    }

    @Override
    public Post getChild(int idx) {
        if (filled) {
            return super.getChild(idx);
        } else {
            throw new IndexOutOfBoundsException("There is no responses on post " + messageData);
        }
    }

    @Override
    public int compareTo(Post o) {
        long postDate = o.getLastPostDate();
        long thisPostDate = getLastPostDate();

        return thisPostDate == postDate ? 0 : postDate > thisPostDate ? 1 : -1;
    }

    @Override
    public int getSize() {
        if (filled) {
            return super.getSize();
        } else {
            return 0;
        }
    }

    public void setLoadingState(LoadingState loadingState) {
        this.loadingState = loadingState;
    }

    @Override
    public LoadingState getLoadingState() {
        return empty ? LoadingState.Loaded : loadingState;
    }

    @Override
    public ReadStatus isRead() {
        return filled ? super.isRead() : readStatus;
    }

    @Override
    public boolean isLeaf() {
        return empty;
    }

    public int compareTo(Thread o) {
        long dateO = o.messageData.getMessageDate();
        long date = messageData.getMessageDate();

        return dateO == date ? 0 : date > dateO ? 1 : -1;
    }

    void storePosts(MessageData... posts) {
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

            Post p = new Post(post, parent, this);
            threadPosts.put(post.getMessageId(), p);

            parent.insertChild(p);
        }

        filled = true;
    }
}
