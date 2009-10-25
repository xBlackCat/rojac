package org.xblackcat.rojac.gui.view.thread;

/**
 * Control class of all threads of the specified forum.
 *
 * @author xBlackCat
 */

public class ForumThreadsControl implements IThreadControl {
    @Override
    public int loadThreadByItem(int itemId, ThreadsModel model) {
        model.setRoot(new ForumRootItem(itemId));

        return itemId;       
    }

    @Override
    public void updateItem(ThreadsModel model, int... itemId) {
    }

    @Override
    public boolean isRootVisible() {
        return false;
    }
}
