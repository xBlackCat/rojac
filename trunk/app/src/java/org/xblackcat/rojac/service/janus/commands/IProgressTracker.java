package org.xblackcat.rojac.service.janus.commands;

import org.xblackcat.rojac.i18n.Message;

/**
 * @author xBlackCat
 */

interface IProgressTracker {
    void addLodMessage(Message message, Object... arguments);

    void postException(Throwable t);

    void updateProgress(int current, int total);
}
