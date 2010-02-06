package org.xblackcat.rojac.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.service.executor.TaskType;
import org.xblackcat.rojac.service.executor.TaskTypeEnum;

import javax.swing.*;

/**
 * Proxy class to catch exceptions during command execution. See {@link javax.swing.SwingWorker} for execution process
 * details.
 *
 * @author xBlackCat
 */
@TaskType(TaskTypeEnum.DataLoading)
public abstract class RojacWorker<T, V> extends SwingWorker<T, V> implements Runnable {
    private static final Log log = LogFactory.getLog(RojacWorker.class);

    /**
     * Implement action method to handle risen exceptions during command execution.
     *
     * @return return object obtained via {@link #perform()} method.
     *
     * @throws Exception exception which was thrown during command execution.
     */
    @Override
    protected final T doInBackground() throws Exception {
        try {
            return perform();
        } catch (Throwable e) {
            log.error("Got exception in working thread.", e);
            RojacUtils.showExceptionDialog(e);
            if (e instanceof Exception) {
                throw (Exception) e;
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return the computed result
     *
     * @throws Exception if unable to compute a result
     */
    protected abstract T perform() throws Exception;
}
