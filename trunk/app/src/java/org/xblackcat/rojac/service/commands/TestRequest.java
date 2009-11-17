package org.xblackcat.rojac.service.commands;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.data.Version;
import org.xblackcat.rojac.gui.dialogs.progress.IProgressTracker;
import org.xblackcat.rojac.service.janus.IJanusService;
import org.xblackcat.rojac.service.janus.JanusServiceException;
import org.xblackcat.rojac.service.janus.data.NewData;

/**
 * Dumb request to Janus service to check if the user is really registered. 
 *
 * @author xBlackCat
 */

public class TestRequest extends ARequest {
    private static final Log log = LogFactory.getLog(TestRequest.class);

    @Override
    public AffectedIds process(IProgressTracker trac, IJanusService janusService) throws RojacException {
        int userId = 0;
        try {
            NewData data = janusService.getNewData(
                    new int[]{-1},
                    true,
                    new Version(),
                    new Version(),
                    new Version(),
                    ArrayUtils.EMPTY_INT_ARRAY,
                    ArrayUtils.EMPTY_INT_ARRAY,
                    1
            );

            userId = data.getOwnUserId();
        } catch (JanusServiceException e) {
            // Login rejected
            userId = 0;
        }
        if (log.isDebugEnabled()) {
            log.debug("Got user id: " + userId);
        }

        return new AffectedIds(new int[] {userId}, ArrayUtils.EMPTY_INT_ARRAY);
    }
}
