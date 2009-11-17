package org.xblackcat.rojac.service.commands;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.data.Message;
import org.xblackcat.rojac.data.Moderate;
import org.xblackcat.rojac.data.Rating;
import org.xblackcat.rojac.gui.dialogs.progress.IProgressTracker;
import org.xblackcat.rojac.service.janus.IJanusService;
import org.xblackcat.rojac.service.janus.JanusServiceException;
import org.xblackcat.rojac.service.janus.data.TopicMessages;
import org.xblackcat.rojac.service.storage.StorageException;

/**
 * Command for loading extra and broken messages.
 *
 * @author xBlackCat
 */

class LoadExtraMessagesRequest extends ALoadPostsRequest {
    private static final Log log = LogFactory.getLog(LoadExtraMessagesRequest.class);

    public AffectedPosts process(IProgressTracker trac, IJanusService janusService) throws RojacException {
        trac.addLodMessage("Loading extra messages started.");

        int[] messageIds = miscAH.getExtraMessages();

        if (!ArrayUtils.isEmpty(messageIds)) {
            trac.addLodMessage("Loading additional messages: " + ArrayUtils.toString(messageIds));
            loadTopics(messageIds, null);

            miscAH.clearExtraMessages();
        }

        int[] brokenTopicIds = mAH.getBrokenTopicIds();
        if (!ArrayUtils.isEmpty(brokenTopicIds)) {
            trac.addLodMessage("Loading broken topics by ids: " + ArrayUtils.toString(brokenTopicIds));
            loadTopics(brokenTopicIds, null);
        }

        trac.addLodMessage("Loading extra messages finished.");        
        return new AffectedPosts(processedMessages, affectedForums);
    }

    private void loadTopics(int[] messageIds, IJanusService janusService) throws RsdnProcessorException, StorageException {
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

        storeNewPosts(messages, moderates, ratings);

        postprocessingMessages();
    }
}