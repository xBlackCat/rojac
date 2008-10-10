package org.xblackcat.rojac.service.synchronizer;

import gnu.trove.TIntHashSet;
import gnu.trove.TIntLongHashMap;
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
import org.xblackcat.rojac.service.storage.IMessageAH;
import org.xblackcat.rojac.service.storage.IMiscAH;
import org.xblackcat.rojac.service.storage.IModerateAH;
import org.xblackcat.rojac.service.storage.IRatingAH;
import org.xblackcat.rojac.service.storage.StorageException;

/**
 * Date: 26 вер 2008
 *
 * @author xBlackCat
 */

public class GetNewPostsCommand extends ARsdnCommand<NewPostsResult> {
    private static final Log log = LogFactory.getLog(GetNewPostsCommand.class);

    public GetNewPostsCommand(IResultHandler<NewPostsResult> iResultHandler) {
        super(iResultHandler);
    }

    public NewPostsResult process(IProgressTracker trac) throws RojacException {
        trac.addLodMessage("Synchronization started.");

        int[] forumIds = storage.getForumAH().getSubscribedForumIds();
        if (ArrayUtils.isEmpty(forumIds)) {
            if (log.isWarnEnabled()) {
                log.warn("You should select at least one forum to start synchronization.");
            }
            return new NewPostsResult();
        }

        if (log.isDebugEnabled()) {
            log.debug("Load new messages for forums [id=" + ArrayUtils.toString(forumIds) + "]");
        }

        Integer limit = optionsService.getProperty(Property.SYNCHRONIZER_LOAD_MESSAGES_PORTION);

        Version messagesVersion = getVersion(VersionType.MESSAGE_ROW_VERSION);
        Version moderatesVersion = getVersion(VersionType.MODERATE_ROW_VERSION);
        Version ratingsVersion = getVersion(VersionType.RATING_ROW_VERSION);

        IRatingAH rAH = storage.getRatingAH();
        IMessageAH mAH = storage.getMessageAH();
        IModerateAH modAH = storage.getModerateAH();

        TIntHashSet processedMessages = new TIntHashSet();
        TIntHashSet affectedForums = new TIntHashSet();

        NewData data;
        int[] brokenTopics = mAH.getBrokenTopicIds();
        do {
            if (!ArrayUtils.isEmpty(brokenTopics)) {
                if (log.isInfoEnabled()) {
                    log.info("Found broken topics: " + ArrayUtils.toString(brokenTopics));
                }
            }

            // Broken topic ids
            TIntHashSet topics = new TIntHashSet(brokenTopics);

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

                for (Message mes : data.getMessages()) {
                    if (mAH.isExist(mes.getMessageId())) {
                        mAH.updateMessage(mes);
                    } else {
                        mAH.storeMessage(mes);
                    }

                    int topicId = mes.getTopicId();
                    if (topicId != 0) {
                        topics.add(topicId);
                    }

                    processedMessages.add(mes.getMessageId());
                    affectedForums.add(mes.getForumId());
                }

                processedMessages.addAll(updateParentsDate(processedMessages.toArray()));

                for (Moderate mod : data.getModerates()) {
                    modAH.storeModerateInfo(mod);
                    processedMessages.add(mod.getMessageId());
                    affectedForums.add(mod.getForumId());
                }
                for (Rating r : data.getRatings()) {
                    rAH.storeRating(r);
                    processedMessages.add(r.getMessageId());
                }

                ratingsVersion = data.getRatingRowVersion();
                messagesVersion = data.getForumRowVersion();
                moderatesVersion = data.getModerateRowVersion();

                setVersion(VersionType.MESSAGE_ROW_VERSION, messagesVersion);
                setVersion(VersionType.MODERATE_ROW_VERSION, moderatesVersion);
                setVersion(VersionType.RATING_ROW_VERSION, ratingsVersion);

            } while (data.getMessages().length > 0);

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

        return new NewPostsResult(processedMessages.toArray(), affectedForums.toArray());
    }

    /**
     * Updates lastChildDate for parents of current messages.
     *
     * @param parentsUpdates trove hash map with pairs of &lt;messageId, date&gt;
     *
     * @return an array of affected messages.
     */
    private int[] updateParentsDate(int[] messageIds) throws StorageException{
        if (ArrayUtils.isEmpty(messageIds)) {
            return ArrayUtils.EMPTY_INT_ARRAY;
        }

        TIntHashSet updatedParents = new TIntHashSet();
        TIntLongHashMap parentDates = new TIntLongHashMap();

        IMessageAH mah = storage.getMessageAH();
        for (int messageId : messageIds) {
            Message m = mah.getMessageById(messageId);

            if (m == null) {
                // Got broken topic - skip
                continue;
            }

            int parentId = m.getParentId();
            if (parentId > 0) {
                long recentDate = m.getResentChildDate();

                updatedParents.add(parentId);

                if (!parentDates.containsKey(parentId) ||
                        parentDates.get(parentId) < recentDate) {
                    parentDates.put(parentId, recentDate);
                }
            }
        }

        for (int messageId : parentDates.keys()) {
            mah.updateMessageRecentDate(messageId, parentDates.get(messageId));
        }

        updatedParents.addAll(updateParentsDate(updatedParents.toArray()));

        return updatedParents.toArray();
    }
}
