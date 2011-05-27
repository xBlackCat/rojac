package org.xblackcat.rojac.gui;

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
    void loadItem(int itemId);

    /**
     * Searches for item by complex key (forumId, messageId). Returns an item if it found and <code>null</code>
     * elsewise.
     *
     * @param messageId
     * @return found item or <code>null</code> if item is not exists in the view.
     */
    boolean containsItem(int messageId);

    /**
     * Opens the item in the view. Make it visible or load it into item if it necessary.
     *
     * @param messageId
     */
    void makeVisible(int messageId);

}
