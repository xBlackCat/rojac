package org.xblackcat.rojac.gui.view.thread;

/**
 * Control of single-thread view.
 *
 * @author xBlackCat
 */

public class SingleThreadControl implements IThreadControl<MessageItem> {
    @Override
    public int loadThreadByItem(AThreadModel<MessageItem> model, int itemId) {
        MessageItem mi = new MessageItem(null, itemId);
        model.setRoot(mi);
        return mi.getMessage(model).getForumId();
    }

    @Override
    public void updateItem(AThreadModel<MessageItem> model, int... itemId) {
    }

    @Override
    public void loadChildren(AThreadModel<MessageItem> threadModel, MessageItem item) {
    }

    @Override
    public boolean isRootVisible() {
        return true;
    }
}
