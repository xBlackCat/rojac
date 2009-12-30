package org.xblackcat.rojac.gui.view.thread;

/**
 * @author xBlackCat
 */

public interface ITreeItem<T extends ITreeItem> extends Comparable<T> {
    int getMessageId();

    T getParent();

    /**
     * See {@linkplain javax.swing.tree.TreeModel#getIndexOfChild(Object, Object) getIndexOfChild} for details.
     *
     * @param node
     * @return
     *
     * @see javax.swing.tree.TreeModel#getIndexOfChild(Object, Object)
     */
    int getIndex(T node);

    LoadingState getLoadingState();

    T getChild(int idx);

    int getSize();

    long getLastPostDate();
}
