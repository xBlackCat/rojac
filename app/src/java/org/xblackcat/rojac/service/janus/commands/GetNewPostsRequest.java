package org.xblackcat.rojac.service.janus.commands;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.data.Version;
import org.xblackcat.rojac.data.VersionType;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.RojacHelper;
import org.xblackcat.rojac.service.janus.IJanusService;
import org.xblackcat.rojac.service.janus.data.NewData;
import ru.rsdn.Janus.JanusMessageInfo;
import ru.rsdn.Janus.JanusModerateInfo;
import ru.rsdn.Janus.JanusRatingInfo;
import ru.rsdn.Janus.RequestForumInfo;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import static org.xblackcat.rojac.service.options.Property.RSDN_USER_ID;
import static org.xblackcat.rojac.service.options.Property.SYNCHRONIZER_LOAD_MESSAGES_PORTION;

/**
 * @author xBlackCat
 */

class GetNewPostsRequest extends ALoadPostsRequest {
    private static final Log log = LogFactory.getLog(GetNewPostsRequest.class);

    public AffectedIds process(IProgressTracker tracker, IJanusService janusService) throws RojacException {
        int[] forumIds = forumAH.getSubscribedForumIds();

        String idsList = Arrays.toString(forumIds);
        tracker.addLodMessage(Messages.SYNCHRONIZE_COMMAND_NAME_NEW_POSTS, idsList);
        if (ArrayUtils.isEmpty(forumIds)) {
            if (log.isWarnEnabled()) {
                log.warn("You should select at least one forum to start synchronization.");
            }
            return new AffectedIds();
        }

        if (log.isDebugEnabled()) {
            log.debug("Load new messages for forums [id=" + idsList + "]");
        }

        Collection<RequestForumInfo> forumInfo = new LinkedList<RequestForumInfo>();
        Map<Integer, Integer> messagesInForums = forumAH.getMessagesInForum(forumIds);
        for (int forumId : forumIds) {
            forumInfo.add(new RequestForumInfo(forumId, messagesInForums.get(forumId) == 0));
        }

        Integer limit = SYNCHRONIZER_LOAD_MESSAGES_PORTION.get();

        tracker.addLodMessage(Messages.SYNCHRONIZE_COMMAND_PORTION, limit);

        Version messagesVersion = RojacHelper.getVersion(VersionType.MESSAGE_ROW_VERSION);
        Version moderatesVersion = RojacHelper.getVersion(VersionType.MODERATE_ROW_VERSION);
        Version ratingsVersion = RojacHelper.getVersion(VersionType.RATING_ROW_VERSION);

        processedMessages.clear();
        affectedForums.clear();

        JanusMessageInfo[] messages;
        do {
            if (ratingsVersion.isEmpty()) {
                ratingsVersion = moderatesVersion;
            }

            NewData data = janusService.getNewData(
                    forumInfo.toArray(new RequestForumInfo[forumInfo.size()]), ratingsVersion,
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
            JanusModerateInfo[] moderates = data.getModerates();
            JanusRatingInfo[] ratings = data.getRatings();

            tracker.addLodMessage(Messages.SYNCHRONIZE_COMMAND_GOT_POSTS, messages.length, moderates.length, ratings.length);

            storeNewPosts(tracker, data);

            ratingsVersion = data.getRatingRowVersion();
            messagesVersion = data.getForumRowVersion();
            moderatesVersion = data.getModerateRowVersion();

            RojacHelper.setVersion(VersionType.MESSAGE_ROW_VERSION, messagesVersion);
            RojacHelper.setVersion(VersionType.MODERATE_ROW_VERSION, moderatesVersion);
            RojacHelper.setVersion(VersionType.RATING_ROW_VERSION, ratingsVersion);

        } while (messages.length == limit);

        tracker.addLodMessage(Messages.SYNCHRONIZE_COMMAND_GOT_USER_ID, RSDN_USER_ID.get());

        return new AffectedIds(processedMessages, affectedForums);
    }

}
