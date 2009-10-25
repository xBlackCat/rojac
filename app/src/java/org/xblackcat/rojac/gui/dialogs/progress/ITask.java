package org.xblackcat.rojac.gui.dialogs.progress;

import org.xblackcat.rojac.RojacException;

/**
 * @author xBlackCat
 */

public interface ITask {
    void doTask(IProgressTracker trac) throws RojacException;
}
