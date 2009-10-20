package org.xblackcat.rojac.gui.frame.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.gui.IRootPane;

/**
 * @author xBlackCat
 */

public class ForumThreadsView extends ATreeThreadView {
    private static final Log log = LogFactory.getLog(ForumThreadsView.class);

    public ForumThreadsView(IRootPane mainFrame) {
        super(mainFrame);
    }

    protected AThreadTreeModel createModel() {
        return new ForumThreadsTreeModel();
    }

    @Override
    protected void initializeLayout() {
        super.initializeLayout();
        messages.setRootVisible(false);
    }

    public void viewItem(int forumId, boolean isNewMessage) {
        model.loadRoot(forumId);
        loadForumInfo(forumId);

        messages.setSelectionRow(0);
    }

    public void updateItem(int messageId) {
    }

}