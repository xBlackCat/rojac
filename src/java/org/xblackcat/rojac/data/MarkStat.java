package org.xblackcat.rojac.data;

/**
 * @author xBlackCat
 */

public class MarkStat {
    private final Mark mark;
    private final int amount;

    public MarkStat(Mark mark, int amount) {
        this.amount = amount;
        this.mark = mark;
    }

    public int getAmount() {
        return amount;
    }

    public Mark getMark() {
        return mark;
    }
}
