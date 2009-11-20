package org.xblackcat.rojac.service.janus.commands;

/**
 * @author xBlackCat
 */

interface IProgressTracker {
    void addLodMessage(String message);

    void postException(Throwable t);
}
