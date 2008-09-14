package org.xblackcat.rojac.gui.frame.progress;

/**
 * Date: 31 ρεπο 2008
 *
 * @author xBlackCat
 */

public interface IProgressTracker {
    void addLodMessage(String message);

    void setProgress(int amount, int total);

    void postException(Throwable t);

    boolean isSuccess();
}
