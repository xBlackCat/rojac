package org.xblackcat.rojac.service.janus.commands;

import org.xblackcat.rojac.i18n.Messages;

/**
 * @author xBlackCat
 */

interface IProgressTracker {
    void addLodMessage(Messages message, Object... arguments);

    void postException(Throwable t);
}
