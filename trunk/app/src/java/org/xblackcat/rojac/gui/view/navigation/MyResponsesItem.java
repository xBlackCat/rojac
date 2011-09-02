package org.xblackcat.rojac.gui.view.navigation;

import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.theme.ReadStatusIcon;
import org.xblackcat.rojac.gui.view.ViewType;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.options.Property;

import javax.swing.*;

/**
 * 10.08.11 17:26
 *
 * @author xBlackCat
 */
class MyResponsesItem extends PersonalItem {
    MyResponsesItem() {
        super(ReadStatusIcon.Response);
    }

    @Override
    JPopupMenu getContextMenu(IAppControl appControl) {
        return null;
    }

    @Override
    void onDoubleClick(IAppControl appControl) {
        Integer userId = Property.RSDN_USER_ID.get();
        if (userId != null) {
            appControl.openTab(ViewType.ReplyList.makeId(userId));
        }
    }

    @Override
    String getExtraInfo() {
        return null;
    }

    @Override
    String getTitleLine() {
        return Message.View_Navigation_Item_MyResponses.get();
    }

    @Override
    String getExtraTitleLine() {
        return null;
    }

    @Override
    String getBriefInfo() {
        return getStat().asString();
    }
}
