package org.xblackcat.rojac.gui.frame.progress;

import org.xblackcat.rojac.RojacException;

/**
 * @author xBlackCat
 */

public interface ITask {
    void doTask(IProgressTracker trac) throws RojacException;
}
