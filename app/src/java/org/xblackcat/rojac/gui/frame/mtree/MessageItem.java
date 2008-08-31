package org.xblackcat.rojac.gui.frame.mtree;

import org.xblackcat.rojac.data.Message;
import org.xblackcat.rojac.data.Rating;

/**
 * Date: 14 груд 2007
 *
 * @author xBlackCat
 */

public class MessageItem {
    public static final MessageItem[] NO_ITEMS = new MessageItem[0];

    private final TreeTableModel model;

    // Hierarhy related fields
    private final MessageItem parent;
    private final int hierarchyLevel;
    private MessageItem[] children = MessageItem.NO_ITEMS;
    private Rating[] ratings;
    private ItemStatus status = ItemStatus.NOT_EXPLORED;
    private boolean expanded;

    // Required data fields
    private final int messageId;

    // Data fields
    // The message object without user message
    private Message message;

    public MessageItem(MessageItem parent, int messageId) {
        this.parent = parent;
        this.messageId = messageId;
        model = parent.model;
        hierarchyLevel = parent.hierarchyLevel + 1;
    }

    public MessageItem(TreeTableModel model, int messageId) {
        this.model = model;
        parent = null;
        hierarchyLevel = 0;
        this.messageId = messageId;
    }

    public MessageItem getParent() {
        return parent;
    }

    /**
     * Returns children of the node. <p color='red'>DO NOT MODIFY THE ARRAY!!!!!</p>
     *
     * @return children of the node.
     */
    public MessageItem[] getChildren() {
        return children;
    }

    public void setChildren(MessageItem[] children) {
        this.children = children;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public int getHierarchyLevel() {
        return hierarchyLevel;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public int getMessageId() {
        return messageId;
    }

    /**
     * Returns message object. The message object has no user message text.
     *
     * @return message object.
     */
    public Message getMessage() {
        return message;
    }

    public Rating[] getRatings() {
        return ratings;
    }

    public void setRatings(Rating[] ratings) {
        this.ratings = ratings;
    }

    public boolean isLeaf() {
        return status == ItemStatus.EXPLORED && children.length == 0;
    }
}
