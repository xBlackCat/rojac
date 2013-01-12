package org.xblackcat.rojac.service.progress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author xBlackCat
 */
public class LoggingProgressListener implements IProgressListener {
    private static final Log log = LogFactory.getLog(LoggingProgressListener.class);

    @Override
    public void progressChanged(ProgressChangeEvent e) {
        if (log.isDebugEnabled()) {
            log.debug("Got event: " + e);
        }
    }
}
