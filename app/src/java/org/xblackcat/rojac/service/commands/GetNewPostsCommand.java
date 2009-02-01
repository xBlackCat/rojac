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
import org.xblackcat.rojac.gui.frame.progress.IProgressTracker;
import org.xblackcat.rojac.service.janus.data.NewData;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IMiscAH;

/**
 * Date: 26 вер 2008
 *
 * @author xBlackCat
 */

public class GetNewPostsCommand extends LoadPostsCommand<AffectedPosts> {
    private static final Log log = LogFactory.getLog(GetNewPostsCommand.class);

    public GetNewPostsCommand(IResultHandler<AffectedPosts> iResultHandler) {
        super(iResultHandler);
    }

    public AffectedPosts process(IProgressTracker trac) throws RojacException {
        trac.addLodMessage("Synchronization started.");

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

        Integer limit = optionsService.getProperty(Property.SYNCHRONIZER_LOAD_MESSAGES_PORTION);

        Version messagesVersion = getVersion(VersionType.MESSAGE_ROW_VERSION);
        Version moderatesVersion = getVersion(VersionType.MODERATE_ROW_VERSION);
        Version ratingsVersion = getVersion(VersionType.RATING_ROW_VERSION);

        processedMessages.clear();
        affectedForums.clear();

        NewData data;
        int[] brokenTopics = mAH.getBrokenTopicIds();
        do {
            if (!ArrayUtils.isEmpty(brokenTopics)) {
                if (log.isInfoEnabled()) {
                    log.info("Found broken topics: " + ArrayUtils.toString(brokenTopics));
                }
            }

            // Broken topic ids
            topics.clear();
            topics.addAll(brokenTopics);

            Message[] messages;
            do {
                if (ratingsVersion.isEmpty()) {
                    ratingsVersion = moderatesVersion;
                }

                data = janusService.getNewData(
                        forumIds,
                        messagesVersion.getBytes().length == 0,
                        ratingsVersion,
                        messagesVersion,
                        moderatesVersion,
                        ArrayUtils.EMPTY_INT_ARRAY,
                        brokenTopics,
                        limit
                );

                brokenTopics = ArrayUtils.EMPTY_INT_ARRAY;

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

            // remove existing ids from downloaded topic ids.
            int[] messageIds = topics.toArray();
            for (int mId : messageIds) {
                if (mAH.isExist(mId)) {
                    topics.remove(mId);
                }
            }

            IMiscAH eAH = storage.getMiscAH();

            topics.addAll(eAH.getExtraMessages());

            eAH.clearExtraMessages();

            brokenTopics = topics.toArray();
        } while (!ArrayUtils.isEmpty(brokenTopics));

        if (log.isInfoEnabled()) {
            log.info("Synchronization complete.");
        }

        return new AffectedPosts(processedMessages.toArray(), affectedForums.toArray());
    }

}
