package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.service.janus.commands.AffectedMessage;

/**
 * Control of single-thread view.
 *
 * @author xBlackCat
 */

public class SortedSingleThreadControl implements IThreadControl<Post> {
    @Override
    public int loadThreadByItem(AThreadModel<Post> model, AffectedMessage itemId) {
//        Post mi = new Post(null, itemId);
//        model.setRoot(mi);
//        return mi.getMessageData().getForumId();
        return 0;
    }

    @Override
    public void updateItem(AThreadModel<Post> model, AffectedMessage... itemId) {
    }

    @Override
    public void loadChildren(AThreadModel<Post> threadModel, Post item) {
    }

    @Override
    public boolean isRootVisible() {
        return true;
    }
}
