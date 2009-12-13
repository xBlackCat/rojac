package org.xblackcat.rojac.gui.view.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.executor.IExecutor;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.StorageException;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Control class of all threads of the specified forum.
 *
 * @author xBlackCat
 */

public class ForumThreadsControl implements IThreadControl<MessageItem> {
    private static final Log log = LogFactory.getLog(ForumThreadsControl.class);

    protected final IStorage storage = ServiceFactory.getInstance().getStorage();
    protected final IExecutor executor = ServiceFactory.getInstance().getExecutor();

    @Override
    public int loadThreadByItem(final AThreadModel<MessageItem> model, final int itemId) {
        final ForumRootItem rootItem = new ForumRootItem(itemId);

        model.setRoot(rootItem);

        SwingWorker sw = new SwingWorker<Void, MessageItem>() {
            @Override
            protected Void doInBackground() throws Exception {
                int[] c;
                try {
                    c = storage.getMessageAH().getTopicMessageIdsByForumId(itemId);
                } catch (StorageException e) {
                    log.error("Can not load topics for forum with id = " + itemId, e);
                    throw e;
                }

                final MessageItem[] cI = new MessageItem[c.length];
                for (int i = 0; i < c.length; i++) {
                    cI[i] = new MessageItem(rootItem, c[i]);
                }

                publish(cI);

                return null;
            }

            @Override
            protected void process(List<MessageItem> chunks) {
                rootItem.setChildren(chunks.toArray(new MessageItem[chunks.size()]));

                model.nodeStructureChanged(rootItem);
            }
        };

        executor.execute(sw);
        return itemId;
    }

    @Override
    public void updateItem(AThreadModel<MessageItem> model, int... itemId) {
    }

    @Override
    public void loadChildren(final AThreadModel<MessageItem> threadModel, final MessageItem item) {
        item.setLoadingState(LoadingState.Loading);
        final int itemId = item.getMessageId();

        executor.execute(new SwingWorker<MessageItem[], Void>() {
            @Override
            protected MessageItem[] doInBackground() throws Exception {
                int[] c;
                try {
                    c = storage.getMessageAH().getMessageIdsByParentId(itemId);
                } catch (StorageException e) {
                    log.error("Can not load message children for id = " + itemId, e);
                    throw e;
                }

                final MessageItem[] cI = new MessageItem[c.length];
                for (int i = 0; i < c.length; i++) {
                    cI[i] = new MessageItem(item, c[i]);
                }
                return cI;
            }

            @Override
            protected void done() {
                if (isDone()) {
                    try {
                        item.children = get();
                    } catch (InterruptedException e) {
                        log.fatal("It finally happens!", e);
                    } catch (ExecutionException e) {
                        log.fatal("It finally happens!", e);
                    }
                    item.setLoadingState(LoadingState.Loaded);                    
                }
                threadModel.nodeStructureChanged(item);
            }
        });
    }

    @Override
    public boolean isRootVisible() {
        return false;
    }
}
