package org.xblackcat.rojac.service.janus.commands;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.data.Message;
import org.xblackcat.rojac.data.Moderate;
import org.xblackcat.rojac.data.Rating;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.janus.IJanusService;
import org.xblackcat.rojac.service.janus.JanusServiceException;
import org.xblackcat.rojac.service.janus.data.TopicMessages;
import org.xblackcat.rojac.service.storage.StorageException;

import java.util.Arrays;

/**
 * Command for loading extra and broken messages.
 *
 * @author xBlackCat
 */

class LoadExtraMessagesRequest extends ALoadPostsRequest {
    private static final Log log = LogFactory.getLog(LoadExtraMessagesRequest.class);

    public AffectedIds process(IProgressTracker trac, IJanusService janusService) throws RojacException {
        int[] messageIds = miscAH.getExtraMessages();

        if (!ArrayUtils.isEmpty(messageIds)) {
            trac.addLodMessage(Messages.SYNCHRONIZE_COMMAND_NAME_EXTRA_POSTS, Arrays.toString(messageIds));
            loadTopics(messageIds, janusService, trac);

            miscAH.clearExtraMessages();
        }

        int[] brokenTopicIds = mAH.getBrokenTopicIds();
        if (!ArrayUtils.isEmpty(brokenTopicIds)) {
            trac.addLodMessage(Messages.SYNCHRONIZE_COMMAND_NAME_BROKEN_TOPICS, Arrays.toString(brokenTopicIds));
            loadTopics(brokenTopicIds, janusService, trac);
        }

        return new AffectedIds(processedMessages, affectedForums);
    }

    private void loadTopics(int[] messageIds, IJanusService janusService, IProgressTracker tracker) throws RsdnProcessorException, StorageException {
        TopicMessages extra;
        try {
            extra = janusService.getTopicByMessage(messageIds);
        } catch (JanusServiceException e) {
            throw new RsdnProcessorException("Can not load extra messages.", e);
        }
        Message[] messages = extra.getMessages();
        Moderate[] moderates = extra.getModerates();
        Rating[] ratings = extra.getRatings();

        processedMessages.clear();
        affectedForums.clear();

        tracker.addLodMessage(Messages.SYNCHRONIZE_COMMAND_GOT_POSTS, messages.length, moderates.length, ratings.length);

        storeNewPosts(tracker, extra);

//        postprocessingMessages();
    }
}