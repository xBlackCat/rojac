package org.xblackcat.rojac.gui;

import java.util.EventListener;

/**
 * @author xBlackCat
 */

public interface IActionListener extends EventListener {
    void itemGotFocus(Integer forumId, Integer messageId);

    void itemLostFocus(Integer forumId, Integer messageId);

    void itemUpdated(Integer forumId, Integer messageId);
}
