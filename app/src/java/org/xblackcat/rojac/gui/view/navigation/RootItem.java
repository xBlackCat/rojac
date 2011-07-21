package org.xblackcat.rojac.gui.view.navigation;

import org.xblackcat.rojac.gui.IAppControl;

import javax.swing.*;
import java.util.Arrays;

/**
 * @author xBlackCat
 */
class RootItem extends AGroupItem {
    protected RootItem(AnItem... items) {
        super(null, null, Arrays.asList(items));
        for (AnItem i : children) {
            i.setParent(this);
        }
    }

    @Override
    JPopupMenu getContextMenu(IAppControl appControl) {
        return null;
    }

    @Override
    void onDoubleClick(IAppControl appControl) {
    }

    @Override
    String getBriefInfo() {
        return null;
    }

    @Override
    String getExtraInfo() {
        return null;
    }

    @Override
    String getTitleLine() {
        return null;
    }

    @Override
    String getExtraTitleLine() {
        return null;
    }

    @Override
    boolean isExuded() {
        return false;
    }
}
