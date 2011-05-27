package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.gui.view.model.ITreeItem;

/**
 * @author xBlackCat
 */

public interface IItemProcessor<T extends ITreeItem<T>> {
    void processItem(T item);
}
