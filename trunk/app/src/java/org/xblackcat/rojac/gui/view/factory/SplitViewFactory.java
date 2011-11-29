package org.xblackcat.rojac.gui.view.factory;

import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.IItemView;
import org.xblackcat.rojac.gui.view.IPostEvent;
import org.xblackcat.rojac.gui.view.ViewId;
import org.xblackcat.rojac.gui.view.message.MessageView;
import org.xblackcat.rojac.gui.view.model.ModelControl;
import org.xblackcat.rojac.gui.view.thread.AThreadView;
import org.xblackcat.rojac.gui.view.thread.ThreadDoubleView;
import org.xblackcat.rojac.gui.view.thread.TreeTableThreadView;

/**
 * 07.10.11 11:05
 *
 * @author xBlackCat
 */
class SplitViewFactory implements IViewFactory {
    private final ModelControl modelControl;
    private final boolean verticalSplit;

    public SplitViewFactory(ModelControl modelControl) {
        this.modelControl = modelControl;
        verticalSplit = true;
    }

    @Override
    public IItemView makeView(ViewId id, IAppControl appControl) {
        final AThreadView threadView = new TreeTableThreadView(id, appControl, modelControl);
        IItemView messageView = new MessageView(
                id,
                appControl,
                new Runnable() {
                    @Override
                    public void run() {
                        threadView.setFocus();
                    }
                },
                new IPostEvent() {
                    @Override
                    public void fired(int postId) {
                        threadView.selectNextPost(postId);
                    }
                }
        );

        return new ThreadDoubleView(threadView, messageView, verticalSplit, appControl);
    }
}
