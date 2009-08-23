package org.xblackcat.rojac.gui.frame.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.service.storage.StorageException;

import javax.swing.*;

/**
 * Date: 20 ρεπο 2008
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
    protected void loadData(final AThreadTreeModel model) {
        synchronized (this) {
            if (forum != null) {
                // Nothing to do
                return;
            }
        }

        executor.execute(new Runnable() {
            public void run() {
                final Forum f;
                try {
                    f = storage.getForumAH().getForumById(messageId);
                } catch (StorageException e) {
                    log.error("Can not load forum info with id = " + messageId, e);
                    return;
                }

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        synchronized (this) {
                            if (forum == null) {
                                forum = f;
                            }
                        }
                        model.nodeChanged(ForumRootItem.this);
                    }
                });
            }
        });
    }

    @Override
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
                    c = storage.getMessageAH().getTopicMessageIdsByForumId(messageId);
                } catch (StorageException e) {
                    log.error("Can not load topics for forum with id = " + messageId, e);
                    return;
                }

                final MessageItem[] cI = new MessageItem[c.length];
                for (int i = 0; i < c.length; i++) {
                    cI[i] = new MessageItem(ForumRootItem.this, c[i]);
                }
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        synchronized (this) {
                            if (children == null) {
                                children = cI;
                            }
                        }

                        model.nodeStructureChanged(ForumRootItem.this);
                    }
                });
            }
        });

    }
}
