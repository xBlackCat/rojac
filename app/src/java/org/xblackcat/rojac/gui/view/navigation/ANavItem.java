package org.xblackcat.rojac.gui.view.navigation;

/**
 * Base class for building tree in navigation view.
 *
 * @author xBlackCat
 */
abstract class ANavItem {
    protected ANavItem parent;

    protected ANavItem(ANavItem parent) {
        this.parent = parent;
    }

    ANavItem getParent() {
        return parent;
    }

    protected void setParent(ANavItem parent) {
        this.parent = parent;
    }

    abstract String getBriefInfo();

    abstract String getExtraInfo();

    abstract String getTitleLine();

    abstract String getExtraTitleLine();

    abstract boolean isGroup();

    abstract int indexOf(ANavItem i);

    abstract ANavItem getChild(int idx);

    abstract int getChildCount();
}
