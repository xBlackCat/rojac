package org.xblackcat.rojac.service.janus.commands;

import org.apache.commons.lang.ArrayUtils;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.janus.IJanusService;
import org.xblackcat.rojac.service.janus.JanusServiceException;
import org.xblackcat.rojac.service.janus.data.TopicMessages;
import org.xblackcat.rojac.service.storage.StorageException;

import java.util.Arrays;

import static org.xblackcat.rojac.service.options.Property.RSDN_USER_ID;

/**
 * Command for loading extra and broken messages.
 *
 * @author xBlackCat
 */

class LoadExtraMessagesRequest extends ALoadPostsRequest {
    public void process(IResultHandler<IPacket> handler, IProgressTracker tracker, IJanusService janusService) throws RojacException {
        int[] messageIds = miscAH.getExtraMessages();

        if (!ArrayUtils.isEmpty(messageIds)) {
            tracker.addLodMessage(Messages.Synchronize_Command_Name_ExtraPosts, Arrays.toString(messageIds));
            loadTopics(messageIds, janusService, tracker);

            miscAH.clearExtraMessages();
        }

        int[] brokenTopicIds = mAH.getBrokenTopicIds();
        if (!ArrayUtils.isEmpty(brokenTopicIds)) {
            tracker.addLodMessage(Messages.Synchronize_Command_Name_BrokenTopics, Arrays.toString(brokenTopicIds));
            loadTopics(brokenTopicIds, janusService, tracker);
        }

        tracker.addLodMessage(Messages.Synchronize_Message_GotUserId, RSDN_USER_ID.get());

        postProcessing(tracker);

        setNotifications(handler);
    }

    private void loadTopics(int[] messageIds, IJanusService janusService, IProgressTracker tracker) throws RsdnProcessorException, StorageException {
        TopicMessages extra;
        try {
            extra = janusService.getTopicByMessage(messageIds);
        } catch (JanusServiceException e) {
            throw new RsdnProcessorException("Can not load extra messages.", e);
        }

        storeNewPosts(tracker, extra);
    }
}
