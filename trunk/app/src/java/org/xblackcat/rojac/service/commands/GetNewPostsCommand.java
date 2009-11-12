package org.xblackcat.rojac.service.commands;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.data.Message;
import org.xblackcat.rojac.data.Moderate;
import org.xblackcat.rojac.data.Rating;
import org.xblackcat.rojac.data.Version;
import org.xblackcat.rojac.data.VersionType;
import org.xblackcat.rojac.gui.dialogs.progress.IProgressTracker;
import org.xblackcat.rojac.service.janus.data.NewData;
import org.xblackcat.rojac.service.options.Property;

/**
 * @author xBlackCat
 */

public class GetNewPostsCommand extends LoadPostsCommand<AffectedPosts> {
    private static final Log log = LogFactory.getLog(GetNewPostsCommand.class);

    public GetNewPostsCommand(IResultHandler<AffectedPosts> iResultHandler) {
        super(iResultHandler);
    }

    public AffectedPosts process(IProgressTracker trac) throws RojacException {
        trac.addLodMessage("Getting new posts started.");

        int[] forumIds = storage.getForumAH().getSubscribedForumIds();
        if (ArrayUtils.isEmpty(forumIds)) {
            if (log.isWarnEnabled()) {
                log.warn("You should select at least one forum to start synchronization.");
            }
            return new AffectedPosts();
        }

        if (log.isDebugEnabled()) {
            log.debug("Load new messages for forums [id=" + ArrayUtils.toString(forumIds) + "]");
        }

        Integer limit = Property.SYNCHRONIZER_LOAD_MESSAGES_PORTION.get();

        Version messagesVersion = getVersion(VersionType.MESSAGE_ROW_VERSION);
        Version moderatesVersion = getVersion(VersionType.MODERATE_ROW_VERSION);
        Version ratingsVersion = getVersion(VersionType.RATING_ROW_VERSION);

        processedMessages.clear();
        affectedForums.clear();

        Message[] messages;
        do {
            if (ratingsVersion.isEmpty()) {
                ratingsVersion = moderatesVersion;
            }

            NewData data = janusService.getNewData(
                    forumIds,
                    messagesVersion.getBytes().length == 0,
                    ratingsVersion,
                    messagesVersion,
                    moderatesVersion,
                    ArrayUtils.EMPTY_INT_ARRAY,
                    ArrayUtils.EMPTY_INT_ARRAY,
                    limit
            );

            Property.RSDN_USER_ID.get(data.getOwnUserId());

            messages = data.getMessages();
            Moderate[] moderates = data.getModerates();
            Rating[] ratings = data.getRatings();

            storeNewPosts(messages, moderates, ratings);

            ratingsVersion = data.getRatingRowVersion();
            messagesVersion = data.getForumRowVersion();
            moderatesVersion = data.getModerateRowVersion();

            setVersion(VersionType.MESSAGE_ROW_VERSION, messagesVersion);
            setVersion(VersionType.MODERATE_ROW_VERSION, moderatesVersion);
            setVersion(VersionType.RATING_ROW_VERSION, ratingsVersion);

        } while (messages.length > 0);


//        postprocessingMessages();

        trac.addLodMessage("Getting new posts finished.");

        return new AffectedPosts(processedMessages.toArray(), affectedForums.toArray());
    }

}
