package org.xblackcat.rojac.gui.view.startpage;

import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.view.model.ReadStatus;

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

    abstract Icon getIcon();

    abstract ReadStatus getReadStatus();

    abstract JPopupMenu getContextMenu(IAppControl appControl);

    abstract void onDoubleClick(IAppControl appControl);

    abstract String getBriefInfo();

    abstract String getTitleLine();

    abstract boolean isExuded();

    abstract boolean isGroup();

    abstract <V extends AnItem> int indexOf(V i);

    abstract AnItem getChild(int idx);

    abstract int getChildCount();
}
