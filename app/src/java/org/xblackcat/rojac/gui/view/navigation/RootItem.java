package org.xblackcat.rojac.gui.view.navigation;

import org.xblackcat.rojac.gui.IAppControl;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author xBlackCat
 */
class RootItem extends AGroupItem<AnItem> {
    private static List<AnItem> collectItems(ADecorator... items) {
        List<AnItem> list = new ArrayList<>();
        for (ADecorator d : items) {
            Collections.addAll(list, d.getItemsList());
        }

        return list;
    }

    protected RootItem(ADecorator... items) {
        super(null, null, collectItems(items));

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
    String getTitleLine() {
        return null;
    }

    @Override
    boolean isExuded() {
        return false;
    }
}
