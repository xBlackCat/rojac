package org.xblackcat.sunaj.gui.model;

import org.xblackcat.sunaj.i18n.Messages;

/**
 * Date: 12 лип 2008
 *
 * @author xBlackCat
 */

public enum ForumViewMode {
    /**
     * Show all the forums in list.
     */
    SHOW_ALL(Messages.VIEW_FORUMS_MODE_SHOWALL_TOOLTIP, Messages.VIEW_FORUMS_MODE_SHOWALL_TEXT),
    /**
     * Show subcribed forums. Also show unsubscribed forums if it has some messages.
     */
    SHOW_NOT_EMPTY(Messages.VIEW_FORUMS_MODE_NOTEMPTY_TOOLTIP, Messages.VIEW_FORUMS_MODE_NOTEMPTY_TEXT),
    /**
     * Show only subscribed forums.
     */
    SHOW_SUBCRIBED(Messages.VIEW_FORUMS_MODE_SUBSCRIBED_TOOLTIP, Messages.VIEW_FORUMS_MODE_SUBSCRIBED_TEXT);
    private final Messages tooltip;
    private final Messages text;

    ForumViewMode(Messages tooltip, Messages text) {
        this.tooltip = tooltip;
        this.text = text;
    }

    public String getTooltip() {
        return tooltip.getMessage();
    }

    public String getText() {
        return text.getMessage();
    }
}
