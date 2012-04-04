package org.xblackcat.rojac.service.janus.commands;

import org.xblackcat.rojac.service.IProgressTracker;
import org.xblackcat.rojac.service.storage.IBatchTracker;

/**
 * 04.04.12 12:20
 *
 * @author xBlackCat
 */
public class BatchTracker implements IBatchTracker {
    private final IProgressTracker tracker;
    private int currentBatch;
    private int totalBatch;

    private int base;
    private int progressTotal;
    private int total;

    public BatchTracker(IProgressTracker tracker) {
        this.tracker = tracker;

        setBatch(0, 1);
    }

    @Override
    public void setBatch(int current, int total) {
        this.currentBatch = current;
        this.totalBatch = total;
        this.total = Integer.MIN_VALUE;
    }

    @Override
    public void postException(Throwable t) {
        tracker.postException(t);
    }

    @Override
    public void updateProgress(int current, int total) {
        if (this.total != total) {
            this.total = total;
            progressTotal = total * totalBatch;
            this.base = total * currentBatch;
        }

        tracker.updateProgress(base + current, progressTotal);
    }
}
