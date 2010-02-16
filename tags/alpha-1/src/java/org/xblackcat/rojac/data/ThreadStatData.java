package org.xblackcat.rojac.data;

/**
 * @author xBlackCat
 */

public class ThreadStatData {
    private final long lastPostDate;
    private final int replyAmount;

    public ThreadStatData(long lastPostDate, int replyAmount) {
        this.lastPostDate = lastPostDate;
        this.replyAmount = replyAmount;
    }

    public long getLastPostDate() {
        return lastPostDate;
    }

    public int getReplyAmount() {
        return replyAmount;
    }
}
