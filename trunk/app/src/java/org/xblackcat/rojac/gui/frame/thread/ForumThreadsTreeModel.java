package org.xblackcat.rojac.gui.frame.thread;

/**
 * @author xBlackCat
 */
class ForumThreadsTreeModel extends AThreadTreeModel {
    public void loadRoot(int itemId) {
        root = new ForumRootItem(itemId);
        fireTreeStructureChanged(this, getPathToRoot(root), null, null);
    }
}