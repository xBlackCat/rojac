package org.xblackcat.rojac.service;

/**
 * 12.03.12 15:51
 *
 * @author xBlackCat
 */
public interface IProgressTracker {
    void postException(Throwable t);

    void updateProgress(int current, int total);
}
