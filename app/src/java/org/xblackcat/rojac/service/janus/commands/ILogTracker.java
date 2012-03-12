package org.xblackcat.rojac.service.janus.commands;

import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.IProgressTracker;

/**
 * @author xBlackCat
 */

interface ILogTracker extends IProgressTracker {
    void addLodMessage(Message message, Object... arguments);
}
