package org.xblackcat.rojac.service.progress;

/**
 * 06.04.12 15:26
 *
 * @author xBlackCat
 */
public class ProgressChangeEventEx extends ProgressChangeEvent {
    private final int subCurrent;
    private final int subTotal;

    public ProgressChangeEventEx(Object source, ProgressState state, long value, long bound, int subCurrent, int subTotal) {
        super(source, state, value, bound);
        this.subTotal = subTotal;
        this.subCurrent = subCurrent;
    }

    public int getSubTotal() {
        return subTotal;
    }

    public int getSubCurrent() {
        return subCurrent;
    }
}
