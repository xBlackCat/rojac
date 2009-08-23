package org.xblackcat.rojac.gui.frame.thread;

/**
 * Date: 22 бер 2008
 *
 * @author xBlackCat
 */
class SingleThreadTreeModel extends AThreadTreeModel {
    public void loadRoot(int itemId) {
        root = new MessageItem(null, itemId);
        fireTreeStructureChanged(this, getPathToRoot(root), null, null);
    }
}
