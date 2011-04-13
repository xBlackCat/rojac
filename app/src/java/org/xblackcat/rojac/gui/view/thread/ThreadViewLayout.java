package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.gui.IViewLayout;

/**
 * @author xBlackCat
 */
class ThreadViewLayout implements IViewLayout {
    private static final long serialVersionUID = 1L;

    private Object toolbarPosition;
    private int toolbarOrientation;

    public ThreadViewLayout(Object toolbarPosition, int toolbarOrientation) {
        this.toolbarPosition = toolbarPosition;
        this.toolbarOrientation = toolbarOrientation;
    }

    public Object getToolbarPosition() {
        return toolbarPosition;
    }

    public int getToolbarOrientation() {
        return toolbarOrientation;
    }
}
