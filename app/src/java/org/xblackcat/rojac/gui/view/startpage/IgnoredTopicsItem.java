package org.xblackcat.rojac.gui.view.startpage;

import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.theme.ReadStatusIcon;
import org.xblackcat.rojac.gui.view.ViewType;
import org.xblackcat.rojac.i18n.Message;

import javax.swing.*;

/**
 * 10.08.11 17:26
 *
 * @author xBlackCat
 */
class IgnoredTopicsItem extends PersonalItem {
    IgnoredTopicsItem() {
        super(ReadStatusIcon.IgnoredThreads);
    }

    @Override
    JPopupMenu getContextMenu(IAppControl appControl) {
        return null;
    }

    @Override
    void onDoubleClick(IAppControl appControl) {
        appControl.openTab(ViewType.IgnoredThreadList.makeId(0));
    }

    @Override
    String getTitleLine() {
        return Message.View_Navigation_Item_Ignored.get();
    }

    @Override
    String getBriefInfo() {
        return String.valueOf(getStat().getTotalMessages());
    }
}
