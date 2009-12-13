package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.data.MessageData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
    protected final Post threadRoot;

    protected List<Post> childrenPosts = new ArrayList<Post>();

    public Post(MessageData messageData, Post parent) {
        this(messageData, parent, null);
    }

    public Post(MessageData messageData, Post parent, Post threadRoot) {
        this(messageData, parent, threadRoot, false);
    }

    public Post(MessageData messageData, Post parent, Post threadRoot, boolean read) {
        this.messageData = messageData;
        this.parent = parent;
        this.read = read;

        if (threadRoot == null) {
            this.threadRoot = parent != null ? parent.threadRoot : this;
        } else {
            this.threadRoot = threadRoot;
        }
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

    protected Post getThreadRoot() {
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

    // State related methods

    @Override
    public int getMessageId() {
        return messageData.getMessageId();
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

    public final ReadStatus isRead() {
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
}
