package org.xblackcat.rojac.gui.view.navigation;

import org.xblackcat.rojac.gui.IAppControl;

import javax.swing.*;

/**
 * Base class for building tree in navigation view.
 *
 * @author xBlackCat
 */
abstract class AnItem {
    protected AnItem parent;

    protected AnItem(AnItem parent) {
        this.parent = parent;
    }

    AnItem getParent() {
        return parent;
    }

    protected void setParent(AnItem parent) {
        this.parent = parent;
    }

    abstract JPopupMenu getContextMenu(IAppControl appControl);

    abstract void onDoubleClick(IAppControl appControl);

    abstract String getBriefInfo();

    abstract String getExtraInfo();

    abstract String getTitleLine();

    abstract String getExtraTitleLine();

    abstract boolean isExuded();

    abstract boolean isGroup();

    abstract int indexOf(AnItem i);

    abstract AnItem getChild(int idx);

    abstract int getChildCount();
}
