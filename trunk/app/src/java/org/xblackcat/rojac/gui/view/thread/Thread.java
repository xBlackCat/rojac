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
    /**
     * State flag to indicate if this thread is filled with posts or not. If the thread is filled - statistic will be
     * calculated from real posts data. A stat data from DB is user in other case.
     */
    private boolean filled = false;
    private LoadingState loadingState = LoadingState.NotLoaded;

    private ThreadStatData threadStatData;
    private int unreadPosts;

    /**
     * Aux constructor for newly created thread.
     *
     * @param messageData
     * @param parent
     */
    Thread(MessageData messageData, ForumRoot parent) {
        this(messageData, new ThreadStatData(messageData.getMessageDate(), 0), 0, parent);
        filled = true;
    }

    public Thread(MessageData messageData, ThreadStatData threadStatData, int unreadPosts, ForumRoot parent) {
        super(messageData, parent, null);
        this.threadStatData = threadStatData;
        this.unreadPosts = unreadPosts;

        threadPosts.put(messageData.getMessageId(), this);
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
    public long getLastPostDate() {
        if (filled) {
            return super.getLastPostDate();
        } else if (threadStatData.getReplyAmount() == 0) {
            return messageData.getMessageDate();
        } else {
            return threadStatData.getLastPostDate();
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
        return threadStatData.getReplyAmount() == 0 ? LoadingState.Loaded : loadingState;
    }

    @Override
    public ReadStatus isRead() {
        if (filled) {
            return super.isRead();
        } else if (read) {
            if (unreadPosts > 0) {
                return ReadStatus.ReadPartially;
            } else {
                return ReadStatus.Read;
            }
        } else {
            return ReadStatus.Unread;
        }
    }

    @Override
    public boolean isLeaf() {
        return filled ? super.isLeaf() : threadStatData.getReplyAmount() == 0;
    }

    @Override
    public Post getMessageById(int messageId) {
        return threadPosts.get(messageId);
    }

    void storePosts(MessageData... posts) {
        Arrays.sort(posts, SORT_BY_PARENTS);

        filled = true;

        for (MessageData post : posts) {
            insertChild(post);
        }
    }

    public void insertChild(MessageData post) {
        if (filled) {
            // The thread was already loaded so just insert the post into it.
            Post parent = threadPosts.get(post.getParentId());

            if (parent == null) {
                if (log.isDebugEnabled()) {
                    log.debug("There is no parent post exists for post " + post);
                }
                return;
            }

            Post p = new Post(post, parent, this);
            threadPosts.put(post.getMessageId(), p);

            parent.insertChild(p);
        } else {
            // Thread is not filled so just update statistics
            // Only for new message.
            if (threadStatData.getLastPostDate() < post.getMessageDate()) {
                threadStatData = new ThreadStatData(
                        post.getMessageDate(),
                        threadStatData.getReplyAmount() + 1
                );

                if (!post.isRead()) {
                    unreadPosts++;
                }
            }
        }
    }
}
