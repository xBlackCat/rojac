package org.xblackcat.rojac.service.executor;

import java.util.List;

/**
 * @author xBlackCat
 */

public interface IExecutor {
    /**
     * Execute target task depends on its type specified in annotation.
     *
     * @param target target to execute.
     */
    void execute(Runnable target);

    /**
     * Aims a timer to execute a task after specified period.
     *
     * @param id     timer id. If the id is already aimed old timer will be stooped and a new one will be created.
     * @param target command to execute.
     * @param delay  delay in milliseconds.
     */
    void setupTimer(String id, Runnable target, long delay);

    void setupPeriodicTask(String id, Runnable target, int period);

    /**
     * Stops a timer by its id.
     *
     * @param id timer id to identify a timer.
     * @return <code>true</code> if timer successfully stopped and <code>false</code> if timer with specified id is not
     *         set.
     */
    boolean killTimer(String id);

    void shutdown();

    List<Runnable> shutdownNow();
}
