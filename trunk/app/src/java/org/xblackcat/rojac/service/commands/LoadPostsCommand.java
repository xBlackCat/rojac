package org.xblackcat.rojac.service.commands;

import gnu.trove.TIntHashSet;
import gnu.trove.TIntObjectHashMap;
import gnu.trove.TIntObjectProcedure;
import org.xblackcat.rojac.data.Message;
import org.xblackcat.rojac.data.Moderate;
import org.xblackcat.rojac.data.Rating;
import org.xblackcat.rojac.service.storage.IMessageAH;
import org.xblackcat.rojac.service.storage.IMiscAH;
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

    private final TIntHashSet loadedMessages = new TIntHashSet();

    protected final TIntObjectHashMap<Long> messageDates = new TIntObjectHashMap<Long>();
    protected final TIntObjectProcedure<Long> updater = new TIntObjectProcedure<Long>() {
        @Override
        public boolean execute(int a, Long b) {
            try {
                mAH.updateMessageRecentDate(a, b);
            } catch (StorageException e) {
                log.warn("Can not update recent date for message [id=" + a + "]", e);
            }
            return true;
        }
    };
    protected final IMiscAH miscAH;

    public LoadPostsCommand(IResultHandler<T> resultHandler) {
        super(resultHandler);
        modAH = storage.getModerateAH();
        mAH = storage.getMessageAH();
        rAH = storage.getRatingAH();
        miscAH = storage.getMiscAH();
    }

    protected void storeNewPosts(Message[] messages, Moderate[] moderates, Rating[] ratings) throws StorageException {
        for (Message mes : messages) {
            int mId = mes.getMessageId();
            if (mAH.isExist(mId)) {
                mAH.updateMessage(mes);
            } else {
                mAH.storeMessage(mes);
            }

            loadedMessages.add(mId);

            long mesDate = mes.getMessageDate();
            messageDates.put(mId, mesDate);

            int topicId = mes.getTopicId();
            if (topicId != 0) {
                topics.add(topicId);


                Long date;

                // Update topic dates
                addMessageDate(topicId, mesDate);

                // Update parent dates
                int parentId = mes.getParentId();
                addMessageDate(parentId, mesDate);

                processedMessages.add(parentId);
                processedMessages.add(topicId);
            }

            processedMessages.add(mId);
            int forumId = mes.getForumId();
            if (forumId > 0) {
                affectedForums.add(forumId);
            }
        }

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

    protected final void postprocessingMessages() throws StorageException {
        updateParentsDate(loadedMessages.toArray());

        if (log.isDebugEnabled()) {
            log.debug("There are " + messageDates.size() + " messages to process");
        }

        // Store new dates
        messageDates.forEachEntry(updater);
    }

    private void addMessageDate(int parentId, long mesDate) {
        Long date;
        date = messageDates.get(parentId);
        if (date == null) {
            messageDates.put(parentId, mesDate);
        } else {
            if (mesDate > date) {
                messageDates.put(parentId, mesDate);
            }
        }
    }

    /**
     * Updates resentChildDate field for parents of a new messages.
     *
     * @param parentsUpdates trove hash map with pairs of &lt;messageId, date&gt;
     *
     * @return an array of affected messages.
     */
    private void updateParentsDate(int[] messageIds) throws StorageException {
        TIntHashSet parents = new TIntHashSet();

        for (int mId : messageIds) {
            if (!messageDates.containsKey(mId)) {
                // Message id is absent
                continue;
            }

            int pmId = mAH.getParentIdByMessageId(mId);

            if (pmId == 0) {
                // Top level
                continue;
            }

            Long date = messageDates.get(mId);

            addMessageDate(pmId, date);

            parents.add(pmId);
            processedMessages.add(pmId);
        }

        if (!parents.isEmpty()) {
            updateParentsDate(parents.toArray());
        }
    }
}
