package org.xblackcat.rojac.gui.view.navigation;

import org.xblackcat.rojac.gui.theme.ReadStatusIcon;
import org.xblackcat.rojac.gui.view.model.ReadStatus;

import javax.swing.*;

abstract class PersonalItem extends AnItem {
    private final ReadStatusIcon icon;
    protected int itemsCount;

    protected PersonalItem(ReadStatusIcon icon) {
        super(null);
        this.icon = icon;
    }

    @Override
    Icon getIcon() {
        return icon != null ? icon.getIcon(getReadStatus()) : null;
    }

    @Override
    ReadStatus getReadStatus() {
        return ReadStatus.Read;
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

    @Override
    String getBriefInfo() {
        return String.valueOf(itemsCount);
    }

    int getItemsCount() {
        return itemsCount;
    }

    void setItemsCount(int itemsCount) {
        this.itemsCount = itemsCount;
    }
}
