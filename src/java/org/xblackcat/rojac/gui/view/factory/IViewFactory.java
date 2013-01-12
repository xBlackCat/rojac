package org.xblackcat.rojac.gui.view.factory;

import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.IItemView;
import org.xblackcat.rojac.gui.view.ViewId;

/**
 * @author xBlackCat
 */
interface IViewFactory {
    IItemView makeView(ViewId id, IAppControl appControl);
}
