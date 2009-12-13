package org.xblackcat.rojac.gui.view.thread;

/**
 * @author xBlackCat
 */

public interface ITreeItem<T extends ITreeItem> {
    int getMessageId();

    T getParent();

    int getIndex(T node);

    LoadingState getLoadingState();

    T getChild(int idx);

    int getSize();
}
