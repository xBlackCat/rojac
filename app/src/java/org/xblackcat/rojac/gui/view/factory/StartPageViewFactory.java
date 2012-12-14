package org.xblackcat.rojac.gui.view.factory;

import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.IItemView;
import org.xblackcat.rojac.gui.view.ViewId;

/**
* 13.12.12 15:36
*
* @author xBlackCat
*/
class StartPageViewFactory implements IViewFactory {

    @Override
    public IItemView makeView(ViewId id, IAppControl appControl) {
        return appControl.getStartPageView();
    }
}
