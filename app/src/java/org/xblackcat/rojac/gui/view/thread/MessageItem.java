package org.xblackcat.rojac.gui.view.thread;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Message;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.executor.IExecutor;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.StorageException;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

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

    public MessageItem[] getChildren(ThreadsModel model) {
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

    public Message getMessage(ThreadsModel model) {
        loadData(model);
        return message;
    }

    protected void loadData(final ThreadsModel model) {
        synchronized (this) {
            if (message != null) {
                // Nothing to do
                return;
            }
        }

        executor.execute(new SwingWorker<Message, Void>() {
            @Override
            protected Message doInBackground() throws Exception {
                try {
                    return storage.getMessageAH().getMessageById(messageId);
                } catch (StorageException e) {
                    log.error("Can not load message with id = " + messageId, e);
                    throw e;
                }
            }

            @Override
            protected void done() {
                synchronized (this) {
                    if (message == null && isDone()) {
                        try {
                            message = get();
                        } catch (InterruptedException e) {
                            log.fatal("It finally happens!", e);
                        } catch (ExecutionException e) {
                            log.fatal("It finally happens!", e);
                        }
                    }
                }
                model.nodeChanged(MessageItem.this);
            }
        });
    }

    protected void loadChildren(final ThreadsModel model) {
        synchronized (this) {
            if (this.children != null) {
                return;
            }
        }

        executor.execute(new SwingWorker<MessageItem[], Void>() {
            @Override
            protected MessageItem[] doInBackground() throws Exception {
                int[] c;
                try {
                    c = storage.getMessageAH().getMessageIdsByParentId(messageId);
                } catch (StorageException e) {
                    log.error("Can not load message children for id = " + messageId, e);
                    throw e;
                }

                final MessageItem[] cI = new MessageItem[c.length];
                for (int i = 0; i < c.length; i++) {
                    cI[i] = new MessageItem(MessageItem.this, c[i]);
                }
                return cI;
            }

            @Override
            protected void done() {
                synchronized (this) {
                    if (children == null && isDone()) {
                        try {
                            children = get();
                        } catch (InterruptedException e) {
                            log.fatal("It finally happens!", e);
                        } catch (ExecutionException e) {
                            log.fatal("It finally happens!", e);
                        }
                    }
                }
                model.nodeStructureChanged(MessageItem.this);
            }
        });
    }
}
