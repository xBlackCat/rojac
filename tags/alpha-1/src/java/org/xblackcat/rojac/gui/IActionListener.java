package org.xblackcat.rojac.gui;

import org.xblackcat.rojac.service.janus.commands.AffectedMessage;

import java.util.EventListener;

/**
 * @author xBlackCat
 */

public interface IActionListener extends EventListener {
    void itemGotFocus(AffectedMessage itemId);

    void itemLostFocus(AffectedMessage itemId);

    void itemUpdated(AffectedMessage itemId);
}
