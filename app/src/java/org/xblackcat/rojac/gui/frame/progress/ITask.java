package org.xblackcat.rojac.gui.frame.progress;

import org.xblackcat.rojac.RojacException;

/**
 * Date: 31 ρεπο 2008
 *
 * @author xBlackCat
 */

public interface ITask {
    void doTask(IProgressTracker trac) throws RojacException;
}
