package org.xblackcat.rojac.service.commands;

import gnu.trove.TIntHashSet;
import gnu.trove.TIntLongHashMap;
import org.apache.commons.lang.ArrayUtils;
import org.xblackcat.rojac.data.Message;
import org.xblackcat.rojac.data.Moderate;
import org.xblackcat.rojac.data.Rating;
import org.xblackcat.rojac.service.storage.IMessageAH;
import org.xblackcat.rojac.service.storage.IModerateAH;
import org.xblackcat.rojac.service.storage.IRatingAH;
import org.xblackcat.rojac.service.storage.StorageException;

/**
 * Date: 1 лют 2009
 *
 * @author xBlackCat
 */

public abstract class LoadPostsCommand<T extends AffectedPosts> extends ARsdnCommand<T> {
    protected final TIntHashSet processedMessages = new TIntHashSet();
    protected final TIntHashSet affectedForums = new TIntHashSet();
    protected final TIntHashSet topics = new TIntHashSet();
    protected final IRatingAH rAH;
    protected final IMessageAH mAH;
    protected final IModerateAH modAH;

    public LoadPostsCommand(IResultHandler<T> resultHandler) {
        super(resultHandler);
        modAH = storage.getModerateAH();
        mAH = storage.getMessageAH();
        rAH = storage.getRatingAH();
    }

    protected void storeNewPosts(Message[] messages, Moderate[] moderates, Rating[] ratings) throws StorageException {
        for (Message mes : messages) {
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
            int forumId = mes.getForumId();
            if (forumId > 0) {
                affectedForums.add(forumId);
            }
        }

//                processedMessages.addAll(updateParentsDate(processedMessages.toArray()));

        for (Moderate mod : moderates) {
            modAH.storeModerateInfo(mod);
            processedMessages.add(mod.getMessageId());
            int forumId = mod.getForumId();
            if (forumId > 0) {
                affectedForums.add(forumId);
            }
        }
        for (Rating r : ratings) {
            rAH.storeRating(r);
            processedMessages.add(r.getMessageId());
        }
    }

    /**
     * Updates lastChildDate for parents of current messages.
     *
     * @param parentsUpdates trove hash map with pairs of &lt;messageId, date&gt;
     *
     * @return an array of affected messages.
     */
    private int[] updateParentsDate(int[] messageIds) throws StorageException {
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
