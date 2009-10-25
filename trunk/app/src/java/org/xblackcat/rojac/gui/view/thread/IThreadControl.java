package org.xblackcat.rojac.gui.view.thread;

/**
 * Interface to avoid sub-classing Thread view class.
 */

public interface IThreadControl {
    /**
     * (Re-)initializes threads model according to type of thread view.
     *
     * @param itemId item id to identify how to initialize model.
     * @param model  model to be initialized.
     *
     * @return forum id the view is belonged to. Used to load forum information.
     */
    int loadThreadByItem(int itemId, ThreadsModel model);

    /**
     * Updates the item information.
     *
     * @param model  item container model.
     * @param itemId item to be updated.
     */
    void updateItem(ThreadsModel model, int... itemId);

    /**
     * Returns root item visibility state.
     * @return <code>true</code> if root item should be visible and <code>false</code> elsewise.
     */
    boolean isRootVisible();
}
