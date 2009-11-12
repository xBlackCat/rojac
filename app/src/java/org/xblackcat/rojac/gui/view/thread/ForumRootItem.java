package org.xblackcat.rojac.gui.view.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.service.storage.StorageException;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

/**
 * Node for holding root messages of the selected forum.
 *
 * @author xBlackCat
 */

public class ForumRootItem extends MessageItem {
    private static final Log log = LogFactory.getLog(ForumRootItem.class);
    private Forum forum;

    public ForumRootItem(int forumId) {
        super(null, forumId);
    }

    @Override
    protected void loadData(final ThreadsModel model) {
        synchronized (this) {
            if (forum != null) {
                // Nothing to do
                return;
            }
        }

        executor.execute(new SwingWorker<Forum, Void>() {
            @Override
            protected Forum doInBackground() throws Exception {
                Forum f;
                try {
                    return storage.getForumAH().getForumById(messageId);
                } catch (StorageException e) {
                    log.error("Can not load forum info with id = " + messageId, e);
                    throw e;
                }
            }

            @Override
            protected void done() {
                synchronized (this) {
                    if (forum == null && isDone()) {
                        try {
                            forum = get();
                        } catch (InterruptedException e) {
                            log.fatal("It finally happens!", e);
                        } catch (ExecutionException e) {
                            log.fatal("It finally happens!", e);
                        }
                    }
                }
                model.nodeChanged(ForumRootItem.this);
            }
        });
    }

    @Override
    protected void loadChildren(final ThreadsModel model) {
        synchronized (this) {
            if (this.children != null) {
                return;
            }
        }

        SwingWorker sw = new SwingWorker<MessageItem[], Void>() {
            @Override
            protected MessageItem[] doInBackground() throws Exception {
                int[] c;
                try {
                    c = storage.getMessageAH().getTopicMessageIdsByForumId(messageId);
                } catch (StorageException e) {
                    log.error("Can not load topics for forum with id = " + messageId, e);
                    throw e;
                }

                final MessageItem[] cI = new MessageItem[c.length];
                for (int i = 0; i < c.length; i++) {
                    cI[i] = new MessageItem(ForumRootItem.this, c[i]);
                }

                return cI;
            }

            @Override
            protected void done() {
                synchronized (this) {
                    if (children == null) {
                        try {
                            children = get();
                        } catch (InterruptedException e) {
                            log.fatal("It finally happens!", e);
                        } catch (ExecutionException e) {
                            log.fatal("It finally happens!", e);
                        }
                    }
                }

                model.nodeStructureChanged(ForumRootItem.this);
            }
        };

        executor.execute(sw);

    }
}
