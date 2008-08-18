package org.xblackcat.sunaj.gui.frame.thread;

import org.apache.commons.lang.ArrayUtils;

/**
 * Date: 23 бер 2008
 *
 * @author xBlackCat
 */

public class MessageItem {
    private final int messageId;
    private final MessageItem parent;
    private MessageItem[] children = NO_ITEMS;
    private static final MessageItem[] NO_ITEMS = new MessageItem[0];

    public MessageItem(MessageItem parent, int messageId) {
        this.parent = parent;
        this.messageId = messageId;
    }

    public int getMessageId() {
        return messageId;
    }

    public MessageItem getParent() {
        return parent;
    }

    public MessageItem[] getChildren() {
        return children;
    }

    public void setChildren(MessageItem[] children) {
        this.children = children;
    }

    public int getIndex(MessageItem node) {
        return ArrayUtils.indexOf(children, node);
    }
}
