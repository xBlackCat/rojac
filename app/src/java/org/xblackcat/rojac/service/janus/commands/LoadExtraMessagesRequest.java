package org.xblackcat.rojac.service.janus.commands;

import org.apache.commons.lang.ArrayUtils;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.janus.IJanusService;
import org.xblackcat.rojac.service.janus.JanusServiceException;
import org.xblackcat.rojac.service.janus.data.TopicMessages;
import org.xblackcat.rojac.service.storage.StorageException;
import ru.rsdn.Janus.JanusMessageInfo;
import ru.rsdn.Janus.JanusModerateInfo;
import ru.rsdn.Janus.JanusRatingInfo;

import java.util.Arrays;

/**
 * Command for loading extra and broken messages.
 *
 * @author xBlackCat
 */

class LoadExtraMessagesRequest extends ALoadPostsRequest {
    public void process(IResultHandler<IPacket> handler, IProgressTracker trac, IJanusService janusService) throws RojacException {
        int[] messageIds = miscAH.getExtraMessages();

        if (!ArrayUtils.isEmpty(messageIds)) {
            trac.addLodMessage(Messages.Synchronize_Command_Name_ExtraPosts, Arrays.toString(messageIds));
            loadTopics(messageIds, janusService, trac);

            miscAH.clearExtraMessages();
        }

        int[] brokenTopicIds = mAH.getBrokenTopicIds();
        if (!ArrayUtils.isEmpty(brokenTopicIds)) {
            trac.addLodMessage(Messages.Synchronize_Command_Name_BrokenTopics, Arrays.toString(brokenTopicIds));
            loadTopics(brokenTopicIds, janusService, trac);
        }

        setNotifications(handler);
    }

    private void loadTopics(int[] messageIds, IJanusService janusService, IProgressTracker tracker) throws RsdnProcessorException, StorageException {
        TopicMessages extra;
        try {
            extra = janusService.getTopicByMessage(messageIds);
        } catch (JanusServiceException e) {
            throw new RsdnProcessorException("Can not load extra messages.", e);
        }
        JanusMessageInfo[] messages = extra.getMessages();
        JanusModerateInfo[] moderates = extra.getModerates();
        JanusRatingInfo[] ratings = extra.getRatings();

        tracker.addLodMessage(Messages.Synchronize_Command_GotPosts, messages.length, moderates.length, ratings.length);

        storeNewPosts(tracker, extra);
    }
}
