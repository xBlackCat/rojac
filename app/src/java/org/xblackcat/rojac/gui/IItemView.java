package org.xblackcat.rojac.gui;

import org.xblackcat.rojac.service.janus.commands.AffectedMessage;

/**
 * Main class of item-related views like message pane or threads view.
 *
 * @author xBlackCat
 */

public interface IItemView extends IView {
    /**
     * (Re-)initializes view and loads all the necessary data into the view.
     *
     * @param itemId item id to identify a new data set for the view.
     */
    void loadItem(AffectedMessage itemId);

    /**
     * Registers a new action listener for the message View to subscribe to events like selected message was changed.
     *
     * @param l a new action listener
     */
    void addActionListener(IActionListener l);

    /**
     * Removes an action listener.
     *
     * @param l a listener to remove.
     */
    void removeActionListener(IActionListener l);
}
