package org.xblackcat.rojac.gui.view.thread;

import gnu.trove.TIntObjectHashMap;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Message;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.executor.IExecutor;
import org.xblackcat.rojac.service.executor.TaskType;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.StorageException;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

/**
 * @author xBlackCat
 */

class MessageItem implements ITreeItem<MessageItem> {
    private static final Log log = LogFactory.getLog(MessageItem.class);

    private final TIntObjectHashMap<MessageItem> items = new TIntObjectHashMap<MessageItem>();

    protected final IStorage storage = ServiceFactory.getInstance().getStorage();
    protected final IExecutor executor = ServiceFactory.getInstance().getExecutor();

    protected final int messageId;
    /**
     * Root of the thread.
     */
    protected final MessageItem root;
    protected final MessageItem parent;

    protected MessageItem[] children = null;
    // Real data.
    private Message message;

    private LoadingState loadingState = LoadingState.NotLoaded;

    public MessageItem(MessageItem parent, int messageId) {
        this.parent = parent;
        this.messageId = messageId;

        if (parent != null) {
            root = parent.getThreadMessage();
            root.items.put(getMessageId(), this);
        } else {
            root = null;
        }
    }

    /**
     * Returns a root of a threads.
     *
     * @return If message have uninitialized root field - the message is root.
     */
    public MessageItem getThreadMessage() {
        return root != null ? root : this;
    }

    @Override
    public int getMessageId() {
        return messageId;
    }

    @Override
    public MessageItem getParent() {
        return parent;
    }

    @Override
    public MessageItem getChild(int idx) {
        return children[idx];
    }

    @Override
    public int getSize() {
        return children == null ? 0 : children.length;
    }

    void setChildren(MessageItem[] children) {
        this.children = children;
    }

    @Override
    public int getIndex(MessageItem node) {
        return ArrayUtils.indexOf(children, node);
    }

    public Message getMessage(AThreadModel model) {
        loadData(model);
        return message;
    }

    public ITreeItem findMessage(int id) {
        if (messageId == id) {
            return this;
        }

        return getThreadMessage().items.get(id);
    }

    protected void loadData(final AThreadModel model) {
        if (messageId == -1) {
            return;
        }

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
        }, TaskType.MessageLoading);
    }

    @Override
    public LoadingState getLoadingState() {
        return loadingState;
    }

    void setLoadingState(LoadingState loadingState) {
        this.loadingState = loadingState;
    }
}
