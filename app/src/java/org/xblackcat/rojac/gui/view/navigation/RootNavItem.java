package org.xblackcat.rojac.gui.view.navigation;

import java.util.Arrays;
import java.util.List;

/**
 * @author xBlackCat
 */
class RootNavItem extends ANavItem {
    private final List<ANavItem> children;

    protected RootNavItem(ANavItem... items) {
        super(null);
        children = Arrays.asList(items);
        for (ANavItem i : children) {
            i.setParent(this);
        }

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

    boolean isGroup() {
        return true;
    }

    int indexOf(ANavItem i) {
        if (i.getParent() != this) {
            // Only strict match!
            return -1;
        }

        return children.indexOf(i);
    }

    @Override
    int getChildCount() {
        return children.size();
    }

    @Override
    ANavItem getChild(int idx) {
        return children.get(idx);
    }
}
