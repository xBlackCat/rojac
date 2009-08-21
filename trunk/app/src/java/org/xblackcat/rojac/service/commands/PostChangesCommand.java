package org.xblackcat.rojac.service.commands;

import gnu.trove.TIntHashSet;
import gnu.trove.TIntObjectHashMap;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.NewMessage;
import org.xblackcat.rojac.data.NewModerate;
import org.xblackcat.rojac.data.NewRating;
import org.xblackcat.rojac.gui.frame.progress.IProgressTracker;
import org.xblackcat.rojac.service.janus.JanusServiceException;
import org.xblackcat.rojac.service.janus.data.PostException;
import org.xblackcat.rojac.service.janus.data.PostInfo;
import org.xblackcat.rojac.service.storage.INewMessageAH;
import org.xblackcat.rojac.service.storage.INewModerateAH;
import org.xblackcat.rojac.service.storage.INewRatingAH;
import org.xblackcat.rojac.service.storage.StorageException;

/**
 * Date: 26 ��� 2008
 *
 * @author xBlackCat
 */

public class PostChangesCommand extends ARsdnCommand<AffectedPosts> {
    private static final Log log = LogFactory.getLog(PostChangesCommand.class);

    public PostChangesCommand(IResultHandler<AffectedPosts> voidIResultHandler) {
        super(voidIResultHandler);
    }

    public AffectedPosts process(IProgressTracker trac) {
        trac.addLodMessage("Synchronization started.");

        try {
            INewRatingAH nrAH = storage.getNewRatingAH();
            INewMessageAH nmeAH = storage.getNewMessageAH();
            INewModerateAH nmoAH = storage.getNewModerateAH();

            NewRating[] newRatings;
            NewMessage[] newMessages;
            NewModerate[] newModerates;
            try {
                newRatings = nrAH.getAllNewRatings();
                newMessages = nmeAH.getAllNewMessages();
                newModerates = nmoAH.getAllNewModerates();
            } catch (StorageException e) {
                throw new RsdnProcessorException("Can not load your changes.", e);
            }

            if (ArrayUtils.isEmpty(newRatings) &&
                    ArrayUtils.isEmpty(newMessages) &&
                    ArrayUtils.isEmpty(newModerates)) {

                if (log.isDebugEnabled()) {
                    log.debug("Nothing to post.");
                }
                return new AffectedPosts();
            }

            // Store forum ids of new messages and message ids of new ratings to return update event
            TIntObjectHashMap<NewMessage> messageForumIds = new TIntObjectHashMap<NewMessage>();

            TIntHashSet messagesToUpdate = new TIntHashSet();

            for (NewRating nr : newRatings) {
                messagesToUpdate.add(nr.getMessageId());
            }

            for (NewMessage nm : newMessages) {
                messageForumIds.put(nm.getLocalMessageId(), nm);
            }

            PostInfo postInfo;
            try {
                janusService.postChanges(newMessages, newRatings, newModerates);
                postInfo = janusService.commitChanges();
            } catch (JanusServiceException e) {
                throw new RsdnProcessorException("Can not post your changes to the RSDN.", e);
            }

            TIntHashSet forumsToUpdate = new TIntHashSet();

            try {
                // Assume that all the ratings are commited.
                nrAH.clearRatings();

                // Remove the commited messages from the storage.
                for (int lmID : postInfo.getCommited()) {
                    nmeAH.removeNewMessage(lmID);
                    forumsToUpdate.add(messageForumIds.get(lmID).getForumId());
                    messagesToUpdate.add(messageForumIds.get(lmID).getParentId());
                }

                // Show all the PostExceptions if any
                for (PostException pe : postInfo.getExceptions()) {
                    if (log.isWarnEnabled()) {
                        log.warn(pe);
                    }
                }
            } catch (StorageException e) {
                throw new RsdnProcessorException("Unable to process the commit response.", e);
            }

            return new AffectedPosts(messagesToUpdate.toArray(), forumsToUpdate.toArray());
        } catch (RsdnProcessorException e) {
            // Log the exception to console.
            trac.postException(e);
            return new AffectedPosts();
        }
    }
}