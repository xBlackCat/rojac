package org.xblackcat.rojac.service.janus.commands;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.i18n.Messages;
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
    private static final Log log = LogFactory.getLog(LoadExtraMessagesRequest.class);

    public AffectedIds process(IProgressTracker trac, IJanusService janusService) throws RojacException {
        int[] messageIds = miscAH.getExtraMessages();

        // Just in case
        processed.clear();
        
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

        return processed;
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

        tracker.addLodMessage(Messages.SYNCHRONIZE_COMMAND_GOT_POSTS, messages.length, moderates.length, ratings.length);

        storeNewPosts(tracker, extra);
    }
}