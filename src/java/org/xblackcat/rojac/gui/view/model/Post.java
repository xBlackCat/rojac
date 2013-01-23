package org.xblackcat.rojac.gui.view.model;

import org.apache.commons.collections.CollectionUtils;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.service.options.Property;

import java.util.*;


/**
 * @author xBlackCat
 */

public class Post implements Comparable<Post> {
    protected MessageData messageData;

    // Tree-related fields
    protected final Post parent;
    protected final Thread threadRoot;

    protected List<Post> childrenPosts = new ArrayList<>();

    public Post(MessageData messageData, Post parent) {
        this(messageData, parent, null);
    }

    public Post(MessageData messageData, Post parent, Thread threadRoot) {
        this.messageData = messageData;
        this.parent = parent;

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

    public Post getChild(int idx) {
        return childrenPosts.get(idx);
    }

    public int getSize() {
        return childrenPosts.size();
    }

    /**
     * Returns real amount of replies on the post wherever the node is in loaded state or not.
     *
     * @return real replies amount.
     */
    public int getPostAmount() {
        int replies = childrenPosts.size();
        for (Post c : childrenPosts) {
            replies += c.getPostAmount();
        }
        return replies;
    }

    public boolean isReply(int userId) {
        return messageData != null && messageData.isReply2User(userId);
    }

    public boolean hasUnreadReply() {
        return hasUnreadReply(Property.RSDN_USER_ID.get());
    }

    protected boolean hasUnreadReply(int userId) {
        if (messageData != null && isReply(userId) && !messageData.isRead()){
            return true;
        }

        for (Post c : childrenPosts) {
            if (c.hasUnreadReply(userId)) {
                return true;
            }
        }

        return false;
    }

    public boolean isIgnored() {
        return threadRoot != null && threadRoot.isIgnored();
    }

    public boolean isIgnoredUser() {
        return messageData.isIgnoredUser();
    }

    public Thread getThreadRoot() {
        return threadRoot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Post)) {
            return false;
        }

        Post post = (Post) o;

        return messageData.getMessageId() == post.messageData.getMessageId();

    }

    @Override
    public int hashCode() {
        return messageData.getMessageId();
    }

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
    public Collection<Post> getSubTreeFlatten() {
        Collection<Post> subPosts = new LinkedList<>();

        fillCollection(subPosts, this);

        return subPosts;
    }

    private static void fillCollection(Collection<Post> list, Post root) {
        if (CollectionUtils.isEmpty(root.childrenPosts)) {
            return;
        }

        list.addAll(root.childrenPosts);

        for (Post p : root.childrenPosts) {
            fillCollection(list, p);
        }
    }

    // State related methods

    public int getMessageId() {
        return messageData.getMessageId();
    }

    public int getForumId() {
        return messageData.getForumId();
    }

    public int getTopicId() {
        return messageData.getTopicId();
    }

    public long getLastPostDate() {
        if (childrenPosts.isEmpty()) {
            return messageData.getMessageDate();
        } else if (childrenPosts.size() == 1) {
            return childrenPosts.get(0).getLastPostDate();
        } else {
            long lastPostDate = 0;
            for (Post p : childrenPosts) {
                lastPostDate = Math.max(p.getLastPostDate(), lastPostDate);
            }

            return lastPostDate;
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
        if (!messageData.isRead()) {
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

    public void setRead(boolean read) {
        messageData = messageData.setRead(read);
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
    }
}
