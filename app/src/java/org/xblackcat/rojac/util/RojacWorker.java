package org.xblackcat.rojac.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.service.executor.TaskType;
import org.xblackcat.rojac.service.executor.TaskTypeEnum;
import org.xblackcat.rojac.service.options.Property;

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
     * Stack trace for debug purposes.
     */
    protected final Throwable sourceStackTrace;

    protected RojacWorker() {
        if (Property.ROJAC_DEBUG_MODE.get()) {
            sourceStackTrace = new Throwable("Track source");
        } else {
            sourceStackTrace = null;
        }
    }

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
            if (sourceStackTrace != null) {
                log.error("Source tracing.", e);
            }
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
