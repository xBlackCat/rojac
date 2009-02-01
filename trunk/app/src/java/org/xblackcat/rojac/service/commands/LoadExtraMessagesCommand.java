package org.xblackcat.rojac.service.commands;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.data.Message;
import org.xblackcat.rojac.data.Moderate;
import org.xblackcat.rojac.data.Rating;
import org.xblackcat.rojac.gui.frame.progress.IProgressTracker;
import org.xblackcat.rojac.service.janus.JanusServiceException;
import org.xblackcat.rojac.service.janus.data.TopicMessages;

/**
 * Date: 26 вер 2008
 *
 * @author xBlackCat
 */

public class LoadExtraMessagesCommand extends LoadPostsCommand<AffectedPosts> {
    private static final Log log = LogFactory.getLog(LoadExtraMessagesCommand.class);

    public LoadExtraMessagesCommand(IResultHandler<AffectedPosts> iResultHandler) {
        super(iResultHandler);
    }

    public AffectedPosts process(IProgressTracker trac) throws RojacException {
        trac.addLodMessage("Synchronization started.");

        int[] messageIds = storage.getMiscAH().getExtraMessages();
        
        if (ArrayUtils.isEmpty(messageIds)) {
            return new AffectedPosts();
        }

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

        return new AffectedPosts(processedMessages.toArray(), affectedForums.toArray());
    }
}