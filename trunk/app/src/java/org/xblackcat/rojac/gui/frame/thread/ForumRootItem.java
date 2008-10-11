package org.xblackcat.rojac.gui.frame.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.service.storage.StorageException;

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
    protected void loadData() {
        Forum f;
        synchronized (this) {
            f = forum;
        }
        if (f != null) {
            // Nothing to do
            return;
        }

        try {
            f = storage.getForumAH().getForumById(messageId);
        } catch (StorageException e) {
            log.error("Can not load forum info with id = " + messageId, e);
            return;
        }

        synchronized (this) {
            if (forum == null) {
                f = forum;
            }
        }
    }

    @Override
    protected void loadChildren() {
        synchronized (this) {
            if (this.children != null) {
                return;
            }
        }
        int [] c;

        try {
            c = storage.getMessageAH().getTopicMessageIdsByForumId(messageId);
        } catch (StorageException e) {
            log.error("Can not load topics for forum with id = " + messageId, e);
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
