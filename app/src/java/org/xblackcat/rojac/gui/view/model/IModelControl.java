package org.xblackcat.rojac.gui.view.model;

import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.OpenMessageMethod;
import org.xblackcat.rojac.gui.view.thread.ThreadToolbarActions;
import org.xblackcat.rojac.service.datahandler.IPacket;

import javax.swing.*;

/**
 * Interface to avoid sub-classing Thread view class.
 */

public interface IModelControl<T extends ITreeItem<T>> {
    /**
     * (Re-)initializes threads model according to type of thread view.
     *
     * @param model  model to be initialized.
     * @param itemId item id to identify how to initialize model.
     * @return forum id the view is belonged to. Used to load forum information.
     */
    void fillModelByItemId(AThreadModel<T> model, int itemId);

    /**
     * Initializes a procedure to load children of the thread. item parameter represents a thread root.
     *
     * @param model
     * @param item
     * @param postProcessor
     */
    void loadThread(AThreadModel<Post> model, Post item, Runnable postProcessor);

    /**
     * Returns root item visibility state.
     *
     * @return <code>true</code> if root item should be visible and <code>false</code> elsewise.
     */
    boolean isRootVisible();

    String getTitle(AThreadModel<T> model);

    /**
     * Process packet with correspond behaviour.
     *
     * @param model         data model to be affected by a packet.
     * @param p             packet to process.
     * @param postProcessor
     * @return <code>true</code> if packet was processed.
     */
    void processPacket(AThreadModel<T> model, IPacket p, Runnable postProcessor);

    /**
     * Returns a root item for the specified node.
     *
     * @param post node in model.
     * @return root item for current view type of the specified node.
     */
    T getTreeRoot(T post);

    /**
     * Builds a popup menu depending on control behavior.
     *
     * @param post       selected post. Base of popup menu.
     * @param appControl link to application control.
     * @return constructed popup menu.
     */
    JPopupMenu getItemMenu(T post, IAppControl appControl);

    /**
     * Flag to indicate if a message could be looked in the view or not.
     *
     * @return <code>true</code> if message could be searched in the view.
     */
    boolean allowSearch();

    void resortModel(AThreadModel<T> model);

    /**
     * Returns an icon to be shown in view title (usually - in tab title)
     *
     * @param model threads model to provide state data.
     * @return an icon or <code>null</code> if no icon is provided.
     */
    Icon getTitleIcon(AThreadModel<T> model);

    /**
     * Returns a popup menu for view title.
     *
     * @param model      threads model to provide state data.
     * @param appControl application control provider.
     * @return popup menu or <code>null</code> if no popup is provided.
     */
    JPopupMenu getTitlePopup(AThreadModel<T> model, IAppControl appControl);

    ThreadToolbarActions[] getToolbar();

    /**
     * Remove detailed info for collapsed threads. If supported.
     *
     * @param model model to act.
     * @param item  target thread to clean up.
     */
    void unloadThread(AThreadModel<T> model, T item);

    /**
     * Method to open a post by double-click
     *
     * @return
     */
    OpenMessageMethod getOpenMessageMethod();
}
