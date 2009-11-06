package org.xblackcat.rojac.service.progress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author xBlackCat
 */
class LoggingProgressListener implements IProgressListener {
    private static final Log log = LogFactory.getLog(LoggingProgressListener.class);

    @Override
    public void stateChanged(ProgressState state, String description) {
        if (log.isDebugEnabled()) {
            log.debug("Progress state changed to " + state + " (" + description + ")");
        }
    }

    @Override
    public void progressChanged(float percentage, String description) {
        if (log.isDebugEnabled()) {
            log.debug("Progress changed to " + percentage + " (" + description + ")");
        }
    }
}
