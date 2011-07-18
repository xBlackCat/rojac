package org.xblackcat.rojac.gui.view.navigation;

import java.util.List;

/**
 * Base class for building tree in navigation view.
 *
 * @author xBlackCat
 */
abstract class ANavItem {
    private final boolean group;
    protected final ANavItem parent;

    protected List<ANavItem> children;

    protected ANavItem(boolean group, ANavItem parent) {
        this.group = group;
        this.parent = parent;
    }

    boolean isGroup() {
        return group;
    }

    ANavItem getParent() {
        return parent;
    }

    void setChildren(List<ANavItem> items) {
        assert group && items != null;

        children = items;
    }

    abstract String getBriefInfo();

    abstract String getExtraInfo();

    abstract String getTitleLine();

    abstract String getExtraTitleLine();
}
