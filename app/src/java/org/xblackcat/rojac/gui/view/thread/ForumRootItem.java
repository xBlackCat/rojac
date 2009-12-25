package org.xblackcat.rojac.gui.view.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Node for holding root messages of the selected forum.
 *
 * @author xBlackCat
 */

class ForumRootItem extends MessageItem {
    private static final Log log = LogFactory.getLog(ForumRootItem.class);

    public ForumRootItem(int forumId) {
        super(null, forumId);
    }

    @Override
    protected void loadData(AThreadModel<MessageItem> model) {
    }
}
