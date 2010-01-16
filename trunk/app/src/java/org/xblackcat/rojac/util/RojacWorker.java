package org.xblackcat.rojac.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.service.executor.TaskType;
import org.xblackcat.rojac.service.executor.TaskTypeEnum;

import javax.swing.*;

/**
 * @author xBlackCat
 */
@TaskType(TaskTypeEnum.DataLoading)
public abstract class RojacWorker<T, V> extends SwingWorker<T, V> implements Runnable {
    private static final Log log = LogFactory.getLog(RojacWorker.class);

    @Override
    protected T doInBackground() throws Exception {
        try {
            return perform();
        } catch (Throwable e) {
            log.error("Got exception in working thread.", e);
            RojacUtils.showExceptionDialog(e);
            throw new RuntimeException(e);
        }
    }

    protected abstract T perform() throws Exception;
}
