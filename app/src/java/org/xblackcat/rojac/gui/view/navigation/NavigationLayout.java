package org.xblackcat.rojac.gui.view.navigation;

import org.xblackcat.rojac.gui.IViewLayout;

/**
 * @author xBlackCat Date: 21.07.11
 */
class NavigationLayout implements IViewLayout {
    private static final long serialVersionUID = 1L;

    private final boolean[] expandedStatus;

    public NavigationLayout(boolean[] expandedStatus) {
        this.expandedStatus = expandedStatus;
    }

    public boolean[] getExpandedStatus() {
        return expandedStatus;
    }
}
