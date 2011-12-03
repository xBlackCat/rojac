package org.xblackcat.rojac.gui.view.navigation;

import org.xblackcat.rojac.data.ReadStatistic;
import org.xblackcat.rojac.gui.theme.ReadStatusIcon;
import org.xblackcat.rojac.gui.view.model.ReadStatus;

import javax.swing.*;

abstract class PersonalItem extends AnItem {
    private final ReadStatusIcon icon;
    private ReadStatistic stat;

    protected PersonalItem(ReadStatusIcon icon) {
        super(null);
        this.icon = icon;
        stat = new ReadStatistic(0, 0, 0);
    }

    @Override
    Icon getIcon() {
        return icon != null ? icon.getIcon(getReadStatus()) : null;
    }

    @Override
    ReadStatus getReadStatus() {
        return isExuded() ? ReadStatus.Unread : ReadStatus.Read;
    }

    @Override
    boolean isGroup() {
        return false;
    }

    @Override
    <V extends AnItem> int indexOf(V i) {
        return 0;
    }

    @Override
    AnItem getChild(int idx) {
        return null;
    }

    @Override
    int getChildCount() {
        return 0;
    }

    protected ReadStatistic getStat() {
        return stat;
    }

    protected void setStat(ReadStatistic stat) {
        this.stat = stat;
    }

    @Override
    boolean isExuded() {
        return stat != null && stat.getUnreadMessages() > 0;
    }
}
