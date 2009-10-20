package org.xblackcat.rojac.service.executor;

/**
 * @author xBlackCat
 */

public interface IExecutor {
    void execute(Runnable target, TaskType type);

    void execute(Runnable target);
}
