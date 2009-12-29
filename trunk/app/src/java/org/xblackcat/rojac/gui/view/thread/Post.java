package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.data.MessageData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static ch.lambdaj.Lambda.*;

/**
 * @author xBlackCat
 */

class Post implements ITreeItem<Post> {
    protected final static Comparator<Post> POST_COMPARATOR = new Comparator<Post>() {
        @Override
        public int compare(Post o1, Post o2) {
            return (int) (o1.messageData.getMessageDate() - o2.messageData.getMessageDate());
        }
    };

    protected MessageData messageData;

    // Data-related fields (RW)
    protected boolean read;

    // Tree-related fields
    protected final Post parent;
    protected final Thread threadRoot;

    protected List<Post> childrenPosts = new ArrayList<Post>();

    public Post(MessageData messageData, Post parent) {
        this(messageData, parent, null);
    }

    public Post(MessageData messageData, Post parent, Thread threadRoot) {
        this.messageData = messageData;
        this.parent = parent;
        this.read = messageData.isRead();

        if (threadRoot == null) {
            this.threadRoot = parent != null ? parent.threadRoot : null;
        } else {
            this.threadRoot = threadRoot;
        }
    }

    public boolean containsId(int messageId) {
        for (Post p : childrenPosts) {
            if (p.getMessageId() == messageId) {
                return true;
            }
        }

        return false;
    }

    // Tree/table related methods.

    public Post getParent() {
        return parent;
    }

    public int getIndex(Post p) {
        return childrenPosts.indexOf(p);
    }

    @Override
    public Post getChild(int idx) {
        return childrenPosts.get(idx);
    }

    @Override
    public int getSize() {
        return childrenPosts.size();
    }

    protected Thread getThreadRoot() {
        return threadRoot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post)) return false;

        Post post = (Post) o;

        return messageData.getMessageId() == post.messageData.getMessageId();

    }

    @Override
    public int hashCode() {
        return messageData.getMessageId();
    }

    @Override
    public int compareTo(Post o) {
        long postDate = o.getMessageData().getMessageDate();
        long thisPostDate = o.getMessageData().getMessageDate();

        return thisPostDate == postDate ? 0 : postDate < thisPostDate ? 1 : -1;
    }

    // State related methods

    @Override
    public int getMessageId() {
        return messageData.getMessageId();
    }

    public long getLastPostDate() {
        if (childrenPosts.isEmpty()) {
            return messageData.getMessageDate();
        } else {
            return max(childrenPosts, on(Post.class).getLastPostDate());
        }
    }

    /**
     * Returns a state of the node: if the node is filled with actual data.
     *
     * @return <code>true</code> if node have been filled with actual data.
     */
    public LoadingState getLoadingState() {
        // Post object unlike to Thread object is always have actual data
        return LoadingState.Loaded;
    }

    public ReadStatus isRead() {
        if (read) {
            return ReadStatus.Read;
        }

        for (Post p : childrenPosts) {
            if (p.isRead() != ReadStatus.Unread) {
                return ReadStatus.ReadPartially;
            }
        }

        return ReadStatus.Unread;
    }

    public boolean isLeaf() {
        return childrenPosts.isEmpty();
    }

    public MessageData getMessageData() {
        return messageData;
    }

    public void insertChild(Post p) {
        childrenPosts.add(p);
        Collections.sort(childrenPosts);
    }
}
