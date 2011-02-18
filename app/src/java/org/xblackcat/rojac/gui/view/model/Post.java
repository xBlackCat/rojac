package org.xblackcat.rojac.gui.view.model;

import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.util.RojacUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static ch.lambdaj.Lambda.*;

/**
 * @author xBlackCat
 */

public class Post implements ITreeItem<Post> {
    protected MessageData messageData;

    // Data-related fields (RW)
    protected boolean read;

    // Tree-related fields
    protected final Post parent;
    protected final Thread threadRoot;

    /**
     * Util flag to indicate that a node is newly created. Should be cleared after view is updated.
     */
    private boolean newNode = true;

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

    public Post getMessageById(int messageId) {
        for (Post p : childrenPosts) {
            if (p.getMessageId() == messageId) {
                return p;
            }
        }

        return null;
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

    /**
     * Returns real amount of replies on the post wherever the node is in loaded state or not.
     *
     * @return real replies amount.
     */
    public int getRepliesAmount() {
        int replies = childrenPosts.size();
        for (Post c : childrenPosts) {
            replies += c.getRepliesAmount();
        }
        return replies;
    }

    public Thread getThreadRoot() {
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
        long thisPostDate = getMessageData().getMessageDate();

        return thisPostDate == postDate ? 0 : postDate < thisPostDate ? 1 : -1;
    }

    /**
     * Return flatten sub-tree.
     *
     * @return array of all the post children.
     */
    public Collection<Post> getChildren() {
        Collection<Post> subPosts = new ArrayList<Post>();
        subPosts.addAll(childrenPosts);
        for (Post p : childrenPosts) {
            subPosts.addAll(p.getChildren());
        }

        return subPosts;
    }

    // State related methods

    @Override
    public int getMessageId() {
        return messageData.getMessageId();
    }

    @Override
    public int getForumId() {
        return messageData.getForumId();
    }

    @Override
    public int getTopicId() {
        return messageData.getTopicId();
    }

    @Override
    public long getLastPostDate() {
        if (childrenPosts.isEmpty()) {
            return messageData.getMessageDate();
        } else if (childrenPosts.size() == 1) {
            return childrenPosts.get(0).getLastPostDate();
        } else {
            return max(childrenPosts, on(ITreeItem.class).getLastPostDate());
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
        if (!read) {
            return ReadStatus.Unread;
        }

        for (Post p : childrenPosts) {
            if (p.isRead() != ReadStatus.Read) {
                return ReadStatus.ReadPartially;
            }
        }

        return ReadStatus.Read;
    }

    public boolean isLeaf() {
        return childrenPosts.isEmpty();
    }

    public MessageData getMessageData() {
        return messageData;
    }

    public void insertChild(Post p) {
        childrenPosts.add(p);
//        resort();
    }

    /**
     * Recursively resort whole path till the node.
     */
    protected void resort() {
        Collections.sort(childrenPosts);
        if (parent != null) {
            parent.resort();
        }
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    protected void setDeepRead(boolean read) {
        setRead(read);
        for (Post p : childrenPosts) {
            p.setDeepRead(read);
        }
    }

    /**
     * Recursively resort whole sub-tree of the posts.
     */
    protected void deepResort() {
        Collections.sort(childrenPosts);

        for (Post child : childrenPosts) {
            child.deepResort();
        }
    }

    public void setMessageData(MessageData messageData) {
        this.messageData = messageData;
        this.read = messageData.isRead();
    }

    public boolean isNewNode() {
        assert RojacUtils.checkThread(true, Post.class);

        return newNode;
    }

    /**
     * Recursively clears 'new node' flag
     */
    public void resetNewFlag() {
        assert RojacUtils.checkThread(true, Post.class);

        newNode = false;

        for (Post p : childrenPosts) {
            p.resetNewFlag();
        }
    }
}
