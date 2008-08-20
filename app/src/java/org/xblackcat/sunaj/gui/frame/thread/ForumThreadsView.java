package org.xblackcat.sunaj.gui.frame.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Date: 22 бер 2008
 *
 * @author xBlackCat
 */

public class ForumThreadsView extends TreeThreadView {
    private static final Log log = LogFactory.getLog(ForumThreadsView.class);

    protected AThreadTreeModel createModel() {
        return new ForumThreadsTreeModel();
    }

    @Override
    protected void initializeLayout() {
        super.initializeLayout();
        messages.setRootVisible(false);
    }

    public void viewItem(int forumId) {
        model.loadRoot(forumId);
        loadForumInfo(forumId);

        messages.setSelectionRow(0);
    }

    public void updateItem(int messageId) {
    }

}