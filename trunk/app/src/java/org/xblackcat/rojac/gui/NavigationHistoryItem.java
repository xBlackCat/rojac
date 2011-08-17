package org.xblackcat.rojac.gui;

import org.xblackcat.rojac.gui.view.ViewId;

/**
 * @author xBlackCat
 */
class NavigationHistoryItem {
    private final ViewId viewId;
    private final IState state;

    NavigationHistoryItem(ViewId viewId, IState state) {
        this.viewId = viewId;
        this.state = state;
    }

    public IState getState() {
        return state;
    }

    public ViewId getViewId() {
        return viewId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NavigationHistoryItem that = (NavigationHistoryItem) o;

        return viewId.equals(that.viewId) && (state == null ? that.state == null : state.equals(that.state));

    }

    @Override
    public int hashCode() {
        int result = viewId.hashCode();
        result = 31 * result + (state != null ? state.hashCode() : 0);
        return result;
    }
}
