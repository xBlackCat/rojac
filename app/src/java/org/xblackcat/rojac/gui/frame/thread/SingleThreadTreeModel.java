package org.xblackcat.rojac.gui.frame.thread;

/**
 * @author xBlackCat
 */
class SingleThreadTreeModel extends AThreadTreeModel {
    public void loadRoot(int itemId) {
        root = new MessageItem(null, itemId);
        fireTreeStructureChanged(this, getPathToRoot(root), null, null);
    }
}
