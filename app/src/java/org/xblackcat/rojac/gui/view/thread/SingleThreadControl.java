package org.xblackcat.rojac.gui.view.thread;

/**
 * Control of single-thread view.
 *
 * @author xBlackCat
 */

public class SingleThreadControl implements IThreadControl {
    @Override
    public int loadThreadByItem(int itemId, ThreadsModel model) {
        MessageItem mi = new MessageItem(null, itemId);
        model.setRoot(mi);
        return mi.getMessage(model).getForumId();
    }

    @Override
    public void updateItem(ThreadsModel model, int... itemId) {
    }

    @Override
    public boolean isRootVisible() {
        return true;
    }
}
