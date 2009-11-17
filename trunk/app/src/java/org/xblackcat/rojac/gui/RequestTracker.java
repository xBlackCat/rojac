package org.xblackcat.rojac.gui;

import org.xblackcat.rojac.gui.dialogs.progress.IProgressTracker;

/**
 * Track progress changes from request commands.
 *
 * @author xBlackCat
 */
class RequestTracker implements IProgressTracker {
    @Override
    public void addLodMessage(String message) {
    }

    @Override
    public void setProgress(int amount, int total) {
    }

    @Override
    public void postException(Throwable t) {
    }

    @Override
    public boolean isSuccess() {
        return false;
    }
}
