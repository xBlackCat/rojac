package org.xblackcat.rojac.gui.view.navigation;

import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.i18n.Message;

import javax.swing.*;

/**
 * 10.08.11 17:26
 *
 * @author xBlackCat
 */
class DraftsItem extends PersonalItem {
    DraftsItem() {
        super(null);
    }

    @Override
    JPopupMenu getContextMenu(IAppControl appControl) {
        return null;
    }

    @Override
    void onDoubleClick(IAppControl appControl) {
    }

    @Override
    String getTitleLine() {
        return Message.View_Navigation_Item_Draft.get();
    }

    @Override
    String getBriefInfo() {
        return getStat().asString();
    }
}
