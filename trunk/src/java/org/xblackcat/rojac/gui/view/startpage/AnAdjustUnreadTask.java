package org.xblackcat.rojac.gui.view.startpage;

/**
 * 10.08.11 17:35
 *
 * @author xBlackCat
 */
abstract class AnAdjustUnreadTask<T extends AnItem> implements ILoadTask<Void> {
    protected final int adjustDelta;
    protected final T item;

    public AnAdjustUnreadTask(T item, int adjustDelta) {
        this.adjustDelta = adjustDelta;
        this.item = item;
    }

    @Override
    public Void doBackground() throws Exception {
        return null;
    }


}
