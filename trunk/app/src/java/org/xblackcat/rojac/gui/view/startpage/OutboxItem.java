package org.xblackcat.rojac.gui.view.startpage;

import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.theme.ReadStatusIcon;
import org.xblackcat.rojac.gui.view.ViewType;
import org.xblackcat.rojac.gui.view.model.ReadStatus;
import org.xblackcat.rojac.i18n.Message;

import javax.swing.*;

/**
 * 10.08.11 17:26
 *
 * @author xBlackCat
 */
class OutboxItem extends PersonalItem {
    OutboxItem() {
        super(ReadStatusIcon.OutboxItem);
    }

    @Override
    JPopupMenu getContextMenu(IAppControl appControl) {
        return null;
    }

    @Override
    void onDoubleClick(IAppControl appControl) {
        appControl.openTab(ViewType.OutBox.makeId(0));
    }

    @Override
    String getTitleLine() {
        return Message.View_Navigation_Item_Outbox.get();
    }

    @Override
    String getBriefInfo() {
        return getStat().asString();
    }

    @Override
    Icon getIcon() {
        ReadStatus status = getStat().getTotalMessages() == 0 ? ReadStatus.Read : getStat().getUnreadMessages() > 0 ? ReadStatus.ReadPartially : ReadStatus.Unread;
        return ReadStatusIcon.OutboxItem.getIcon(status);
    }
}
