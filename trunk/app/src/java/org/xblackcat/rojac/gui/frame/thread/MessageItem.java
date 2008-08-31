package org.xblackcat.rojac.gui.frame.thread;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Message;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.StorageException;

/**
 * Date: 23 бер 2008
 *
 * @author xBlackCat
 */

public class MessageItem {
    private static final Log log = LogFactory.getLog(MessageItem.class);

    private static final MessageItem[] NO_ITEMS = new MessageItem[0];

    protected final int messageId;
    protected final MessageItem parent;
    protected MessageItem[] children = null;

    // Real data.
    private Message message;
    private String parsedText;

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
        if (children == null) {
            // Load children
            loadChildren();
        }
        return children;
    }

    public int getIndex(MessageItem node) {
        return ArrayUtils.indexOf(children, node);
    }

    public Message getMessage() {
        loadData();
        return message;
    }

    protected void loadData() {
        Message m;
        synchronized (this) {
            m = message;
        }
        if (m != null) {
            // Nothing to do
            return;
        }
        IStorage s = ServiceFactory.getInstance().getStorage();

        try {
            m = s.getMessageAH().getMessageById(messageId);
        } catch (StorageException e) {
            log.error("Can not load message with id = " + messageId, e);
            return;
        }

        synchronized (this) {
            if (message == null) {
                message = m;
            }
        }
    }
    
    protected void loadChildren() {
        synchronized (this) {
            if (this.children != null) {
                return;
            }
        }
        int [] c;
        IStorage s = ServiceFactory.getInstance().getStorage();

        try {
            c = s.getMessageAH().getMessageIdsByParentId(messageId);
        } catch (StorageException e) {
            log.error("Can not load message children for id = " + messageId, e);
            return;
        }

        MessageItem[] cI = new MessageItem[c.length];
        for (int i = 0; i < c.length; i++) {
            cI[i] = new MessageItem(this, c[i]);
        }
        synchronized (this) {
            if (children == null) {
                children = cI;
            }
        }
    }
}
