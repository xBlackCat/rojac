package org.xblackcat.rojac.gui.view;

import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.IItemView;

/**
* @author xBlackCat
*/
interface IViewFactory {
    IItemView makeView(ViewId id, IAppControl appControl);
}
