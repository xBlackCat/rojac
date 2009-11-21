package org.xblackcat.rojac.service.janus.commands;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.data.Message;
import org.xblackcat.rojac.data.Moderate;
import org.xblackcat.rojac.data.Rating;
import org.xblackcat.rojac.data.Version;
import org.xblackcat.rojac.data.VersionType;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.RojacHelper;
import org.xblackcat.rojac.service.janus.IJanusService;
import org.xblackcat.rojac.service.janus.data.NewData;

import java.util.Arrays;

import static org.xblackcat.rojac.service.options.Property.RSDN_USER_ID;
import static org.xblackcat.rojac.service.options.Property.SYNCHRONIZER_LOAD_MESSAGES_PORTION;

/**
 * @author xBlackCat
 */

class GetNewPostsRequest extends ALoadPostsRequest {
    private static final Log log = LogFactory.getLog(GetNewPostsRequest.class);

    public AffectedIds process(IProgressTracker trac, IJanusService janusService) throws RojacException {
        int[] forumIds = forumAH.getSubscribedForumIds();

        trac.addLodMessage(Messages.SYNCHRONIZE_COMMAND_NAME_NEW_POSTS, Arrays.toString(forumIds));
        if (ArrayUtils.isEmpty(forumIds)) {
            if (log.isWarnEnabled()) {
                log.warn("You should select at least one forum to start synchronization.");
            }
            return new AffectedIds();
        }

        if (log.isDebugEnabled()) {
            log.debug("Load new messages for forums [id=" + ArrayUtils.toString(forumIds) + "]");
        }

        Integer limit = SYNCHRONIZER_LOAD_MESSAGES_PORTION.get();

        trac.addLodMessage(Messages.SYNCHRONIZE_COMMAND_PORTION, limit);
        
        Version messagesVersion = RojacHelper.getVersion(VersionType.MESSAGE_ROW_VERSION);
        Version moderatesVersion = RojacHelper.getVersion(VersionType.MODERATE_ROW_VERSION);
        Version ratingsVersion = RojacHelper.getVersion(VersionType.RATING_ROW_VERSION);

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

            if (data.getOwnUserId() != 0) {
                RSDN_USER_ID.set(data.getOwnUserId());
            }

            messages = data.getMessages();
            Moderate[] moderates = data.getModerates();
            Rating[] ratings = data.getRatings();

            trac.addLodMessage(Messages.SYNCHRONIZE_COMMAND_GOT_POSTS, messages.length, moderates.length, ratings.length);

            storeNewPosts(messages, moderates, ratings);

            ratingsVersion = data.getRatingRowVersion();
            messagesVersion = data.getForumRowVersion();
            moderatesVersion = data.getModerateRowVersion();

            RojacHelper.setVersion(VersionType.MESSAGE_ROW_VERSION, messagesVersion);
            RojacHelper.setVersion(VersionType.MODERATE_ROW_VERSION, moderatesVersion);
            RojacHelper.setVersion(VersionType.RATING_ROW_VERSION, ratingsVersion);

        } while (messages.length > 0);

        trac.addLodMessage(Messages.SYNCHRONIZE_COMMAND_GOT_USER_ID, RSDN_USER_ID.get());

//        postprocessingMessages();

        return new AffectedIds(processedMessages, affectedForums);
    }

}
