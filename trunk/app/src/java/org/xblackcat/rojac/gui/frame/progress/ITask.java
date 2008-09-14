package org.xblackcat.rojac.gui.frame.progress;

/**
 * Date: 31 ρεπο 2008
 *
 * @author xBlackCat
 */

public interface ITask {
    void doTask(IProgressTracker trac) throws Exception;

    void prepareTask() throws Exception;
}
