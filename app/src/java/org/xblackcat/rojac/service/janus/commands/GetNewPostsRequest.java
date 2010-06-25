package org.xblackcat.rojac.service.janus.commands;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.data.Version;
import org.xblackcat.rojac.data.VersionType;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.janus.IJanusService;
import org.xblackcat.rojac.service.janus.data.NewData;
import ru.rsdn.Janus.JanusMessageInfo;
import ru.rsdn.Janus.JanusModerateInfo;
import ru.rsdn.Janus.JanusRatingInfo;
import ru.rsdn.Janus.RequestForumInfo;

import java.util.*;

import static org.xblackcat.rojac.service.options.Property.RSDN_USER_ID;
import static org.xblackcat.rojac.service.options.Property.SYNCHRONIZER_LOAD_MESSAGES_PORTION;

/**
 * @author xBlackCat
 */

class GetNewPostsRequest extends ALoadPostsRequest {
    private static final Log log = LogFactory.getLog(GetNewPostsRequest.class);

    public AffectedMessage[] process(IProgressTracker tracker, IJanusService janusService) throws RojacException {
        int[] forumIds = forumAH.getSubscribedForumIds();

        String idsList = Arrays.toString(forumIds);
        tracker.addLodMessage(Messages.SYNCHRONIZE_COMMAND_NAME_NEW_POSTS, idsList);
        if (ArrayUtils.isEmpty(forumIds)) {
            if (log.isWarnEnabled()) {
                log.warn("You should select at least one forum to start synchronization.");
            }
            return AffectedMessage.EMPTY;
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

        Version messagesVersion = DataHelper.getVersion(VersionType.MESSAGE_ROW_VERSION);
        Version moderatesVersion = DataHelper.getVersion(VersionType.MODERATE_ROW_VERSION);
        Version ratingsVersion = DataHelper.getVersion(VersionType.RATING_ROW_VERSION);

        Set<AffectedMessage> result = new HashSet<AffectedMessage>();
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

            result.addAll(storeNewPosts(tracker, data));

            ratingsVersion = data.getRatingRowVersion();
            messagesVersion = data.getForumRowVersion();
            moderatesVersion = data.getModerateRowVersion();

            DataHelper.setVersion(VersionType.MESSAGE_ROW_VERSION, messagesVersion);
            DataHelper.setVersion(VersionType.MODERATE_ROW_VERSION, moderatesVersion);
            DataHelper.setVersion(VersionType.RATING_ROW_VERSION, ratingsVersion);

        } while (messages.length == limit);

        tracker.addLodMessage(Messages.SYNCHRONIZE_COMMAND_GOT_USER_ID, RSDN_USER_ID.get());

        return result.toArray(new AffectedMessage[result.size()]);
    }

}
