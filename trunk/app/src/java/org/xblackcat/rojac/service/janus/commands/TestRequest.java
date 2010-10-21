package org.xblackcat.rojac.service.janus.commands;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.data.Version;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.janus.IJanusService;
import org.xblackcat.rojac.service.janus.JanusServiceException;
import org.xblackcat.rojac.service.janus.data.NewData;
import ru.rsdn.Janus.RequestForumInfo;

/**
 * Dumb request to Janus service to check if the user is really registered.
 *
 * @author xBlackCat
 */

class TestRequest extends ARequest {
    private static final Log log = LogFactory.getLog(TestRequest.class);

    private static final RequestForumInfo[] DUMMY_REQUEST_INFO = new RequestForumInfo[]{
            new RequestForumInfo(-1, true)
    };

    @Override
    public AffectedMessage[] process(IProgressTracker trac, IJanusService janusService) throws RojacException {
        trac.addLodMessage(Messages.Synchronize_Command_Name_Test);

        int userId;
        try {
            NewData data = janusService.getNewData(
                    DUMMY_REQUEST_INFO,
                    new Version(),
                    new Version(),
                    new Version(),
                    ArrayUtils.EMPTY_INT_ARRAY,
                    ArrayUtils.EMPTY_INT_ARRAY,
                    1
            );

            userId = data.getOwnUserId();
            trac.addLodMessage(Messages.Synchronize_Command_GotUserId, userId);
        } catch (JanusServiceException e) {
            // Login rejected
            userId = 0;
        }
        if (log.isDebugEnabled()) {
            log.debug("Got user id: " + userId);
        }

        return new AffectedMessage[]{new AffectedMessage(userId)};
    }
}
