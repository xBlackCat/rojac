package org.xblackcat.rojac.gui.view.thread;

/**
 * @author xBlackCat
 */

public interface IItemProcessor<T extends ITreeItem<T>> {
    void processItem(T item);
}
