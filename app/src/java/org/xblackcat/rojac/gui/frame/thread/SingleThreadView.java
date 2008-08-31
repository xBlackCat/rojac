package org.xblackcat.rojac.gui.frame.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Date: 22 бер 2008
 *
 * @author xBlackCat
 */

public class SingleThreadView extends TreeThreadView {
    private static final Log log = LogFactory.getLog(SingleThreadView.class);

    protected AThreadTreeModel createModel() {
        return new SingleThreadTreeModel();
    }

    public void viewItem(int rootMessageId) {
        model.loadRoot(rootMessageId);
        MessageItem mi = (MessageItem) model.getRoot();
        loadForumInfo(mi.getMessage().getForumId());

        messages.setSelectionRow(0);
    }

    public void updateItem(int messageId) {
    }

}
