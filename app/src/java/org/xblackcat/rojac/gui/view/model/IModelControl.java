package org.xblackcat.rojac.gui.view.model;

import org.xblackcat.rojac.gui.view.thread.IItemProcessor;
import org.xblackcat.rojac.service.datahandler.IPacket;

/**
 * Interface to avoid sub-classing Thread view class.
 */

public interface IModelControl<T extends ITreeItem<T>> {
    /**
     * (Re-)initializes threads model according to type of thread view.
     *
     * @param model  model to be initialized.
     * @param itemId item id to identify how to initialize model.
     *
     * @return forum id the view is belonged to. Used to load forum information.
     */
    void fillModelByItemId(AThreadModel<T> model, int itemId);

    void markForumRead(AThreadModel<T> model, boolean read);

    void markThreadRead(AThreadModel<T> model, int threadRootId, boolean read);

    void markPostRead(AThreadModel<T> model, int postId, boolean read);

    /**
     * Initializes a procedure to load children of the thread. item parameter represents a thread root.
     *
     * @param model
     * @param item
     * @param postProcessor
     */
    void loadThread(AThreadModel<T> model, T item, IItemProcessor<T> postProcessor);

    /**
     * Returns root item visibility state.
     *
     * @return <code>true</code> if root item should be visible and <code>false</code> elsewise.
     */
    boolean isRootVisible();

    /**
     * Updates model according to Synchronization packet.
     *
     * @param model     model to handle
     * @param threadIds id list of updated threads.
     */
    void updateModel(AThreadModel<T> model, int... threadIds);

    String getTitle(AThreadModel<T> model);

    /**
     * Process packet with correspond behaviour.
     *
     * @param model data model to be affected by a packet.
     * @param p     packet to process.
     *
     * @return <code>true</code> if packet was processed.
     */
    boolean processPacket(AThreadModel<T> model, IPacket p);

    /**
     * Returns a root item for the specified node.
     *
     * @param post node in model.
     *
     * @return root item for current view type of the specified node.
     */
    T getTreeRoot(T post);
}
