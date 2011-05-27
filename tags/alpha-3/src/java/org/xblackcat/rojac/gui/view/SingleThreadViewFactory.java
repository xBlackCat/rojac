package org.xblackcat.rojac.gui.view;

import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.IItemView;
import org.xblackcat.rojac.gui.view.message.MessageView;
import org.xblackcat.rojac.gui.view.model.SingleModelControl;
import org.xblackcat.rojac.gui.view.thread.ThreadDoubleView;
import org.xblackcat.rojac.gui.view.thread.TreeTableThreadView;

/**
 * @author xBlackCat
 */

class SingleThreadViewFactory implements IViewFactory {
    @Override
    public IItemView makeView(ViewId id, IAppControl appControl) {
        IItemView threadView = new TreeTableThreadView(id, appControl, new SingleModelControl());
        IItemView messageView = new MessageView(id, appControl);

        return new ThreadDoubleView(threadView, messageView, true, appControl);
    }
}
