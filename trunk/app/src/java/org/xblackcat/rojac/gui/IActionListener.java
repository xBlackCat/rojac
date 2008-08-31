package org.xblackcat.rojac.gui;

import java.util.EventListener;

/**
 * Date: 17 лип 2008
 *
 * @author xBlackCat
 */

public interface IActionListener extends EventListener {
    void itemGotFocus(int itemId);

    void itemLostFocus(int itemId);

    void itemUpdated(int itemId);
}
