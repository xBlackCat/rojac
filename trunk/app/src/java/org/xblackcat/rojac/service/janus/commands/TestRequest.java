package org.xblackcat.rojac.service.janus.commands;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.data.Version;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.janus.IJanusService;
import org.xblackcat.rojac.service.janus.JanusServiceException;
import org.xblackcat.rojac.service.janus.data.NewData;
import ru.rsdn.janus.RequestForumInfo;

import java.util.Collection;
import java.util.Collections;

/**
 * Dumb request to Janus service to check if the user is really registered.
 *
 * @author xBlackCat
 */

class TestRequest extends ARequest<Integer> {
    private static final Log log = LogFactory.getLog(TestRequest.class);

    private static final Collection<RequestForumInfo> DUMMY_REQUEST_INFO ;

    static {
        RequestForumInfo rfi = new RequestForumInfo();
        rfi.setForumId(-1);
        rfi.setIsFirstRequest(true);
        DUMMY_REQUEST_INFO = Collections.singleton(rfi);
    }

    @Override
    public void process(IResultHandler<Integer> handler, ILogTracker trac, IJanusService janusService) throws RojacException {
        trac.addLodMessage(Message.Synchronize_Command_Name_Test);

        Integer userId;
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
            trac.addLodMessage(Message.Synchronize_Message_GotUserId, userId);
        } catch (JanusServiceException e) {
            // Login rejected
            userId = null;
        }
        if (log.isDebugEnabled()) {
            log.debug("Got user id: " + userId);
        }

        handler.process(userId);
    }
}
