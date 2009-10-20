package org.xblackcat.rojac.gui.frame.thread;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Message;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.executor.IExecutor;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.StorageException;

import javax.swing.*;

/**
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
    protected final IStorage storage = ServiceFactory.getInstance().getStorage();
    protected final IExecutor executor = ServiceFactory.getInstance().getExecutor();

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

    public MessageItem[] getChildren(AThreadTreeModel model) {
        if (children == null) {
            // Load children
            loadChildren(model);
            return NO_ITEMS;
        }
        return children;
    }

    public int getIndex(MessageItem node) {
        return ArrayUtils.indexOf(children, node);
    }

    public Message getMessage(AThreadTreeModel model) {
        loadData(model);
        return message;
    }

    protected void loadData(final AThreadTreeModel model) {
        synchronized (this) {
            if (message != null) {
                // Nothing to do
                return;
            }
        }

        executor.execute(new Runnable() {
            public void run() {
                final Message m;
                try {
                    m = storage.getMessageAH().getMessageById(messageId);
                } catch (StorageException e) {
                    log.error("Can not load message with id = " + messageId, e);
                    return;
                }

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        synchronized (this) {
                            if (message == null) {
                                message = m;
                            }
                        }
                        model.nodeChanged(MessageItem.this);
                    }
                });
            }
        });
    }

    protected void loadChildren(final AThreadTreeModel model) {
        synchronized (this) {
            if (this.children != null) {
                return;
            }
        }

        executor.execute(new Runnable() {
            public void run() {
                int[] c;
                try {
                    c = storage.getMessageAH().getMessageIdsByParentId(messageId);
                } catch (StorageException e) {
                    log.error("Can not load message children for id = " + messageId, e);
                    return;
                }

                final MessageItem[] cI = new MessageItem[c.length];
                for (int i = 0; i < c.length; i++) {
                    cI[i] = new MessageItem(MessageItem.this, c[i]);
                }
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        synchronized (this) {
                            if (children == null) {
                                children = cI;
                            }
                        }
                        model.nodeStructureChanged(MessageItem.this);
                    }
                });
            }
        });
    }
}
