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
    private int totalBatches;

    private long base;
    private long progressTotal;
    private long total;

    private final int totalSuperBatches;
    private int currentSuperBatch;

    public BatchTracker(IProgressTracker tracker) {
        this(tracker, 1);
    }

    public BatchTracker(IProgressTracker tracker, int totalSuperBatches) {
        this.tracker = tracker;
        this.totalSuperBatches = totalSuperBatches;
        currentSuperBatch = 0;

        setBatch(0, 1);
    }

    @Override
    public void setBatch(int current, int total) {
        this.currentBatch = current;
        this.totalBatches = total;
        this.total = Long.MIN_VALUE;
    }

    public void nextSuperBatch() {
        currentSuperBatch++;
        this.total = Long.MIN_VALUE;
    }

    @Override
    public void postException(Throwable t) {
        tracker.postException(t);
    }

    @Override
    public void updateProgress(long current, long total) {
        if (this.total != total) {
            this.total = total;
            progressTotal = total * totalBatches * totalSuperBatches;
            this.base = total * (currentBatch + currentSuperBatch * totalBatches);
        }

        tracker.updateProgress(base + current, progressTotal);
    }
}
