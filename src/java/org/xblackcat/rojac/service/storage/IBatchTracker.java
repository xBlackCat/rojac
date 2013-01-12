package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.service.IProgressTracker;

/**
 * 04.04.12 11:35
 *
 * @author xBlackCat
 */
public interface IBatchTracker extends IProgressTracker {
    void setBatch(int current, int total);
}
