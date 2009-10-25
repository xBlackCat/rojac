package org.xblackcat.rojac.gui.dialogs.progress;

/**
 * @author xBlackCat
 */

public interface IProgressTracker {
    void addLodMessage(String message);

    void setProgress(int amount, int total);

    void postException(Throwable t);

    boolean isSuccess();
}
