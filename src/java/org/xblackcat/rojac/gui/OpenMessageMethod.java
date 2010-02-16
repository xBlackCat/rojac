package org.xblackcat.rojac.gui;

/**
 * @author xBlackCat
 */

public enum OpenMessageMethod {
    /**
     * Message will be opened in own forum tab. The tab will be opened if it not exists.
     */
    Default,
    /**
     * Message will be opened in focused message view.
     */
    ThisView,
    /**
     * Open a new tab with message view and show the message there.
     */
    NewTab
}
