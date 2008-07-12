package org.xblackcat.sunaj.gui.model;

/**
 * Date: 12 лип 2008
 *
 * @author xBlackCat
 */

public enum ForumListMode {
    /**
     * Show all the forums in list.
     */
    SHOW_ALL,
    /**
     * Show subcribed forums. Also show unsubscribed forums if it has some messages.
     */
    SHOW_NOT_EMPTY,
    /**
     * Show only subscribed forums.
     */
    SHOW_SUBCRIBED
}
