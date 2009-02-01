package org.xblackcat.rojac.service.commands;

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
 * Date: 26 вер 2008
 *
 * @author xBlackCat
 */

public class PostChangesCommand extends ARsdnCommand<Boolean> {
    private static final Log log = LogFactory.getLog(PostChangesCommand.class);

    public PostChangesCommand(IResultHandler<Boolean> voidIResultHandler) {
        super(voidIResultHandler);
    }

    public Boolean process(IProgressTracker trac) {
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
                return false;
            }

            PostInfo postInfo;
            try {
                janusService.postChanges(newMessages, newRatings, newModerates);
                postInfo = janusService.commitChanges();
            } catch (JanusServiceException e) {
                throw new RsdnProcessorException("Can not post your changes to the RSDN.", e);
            }

            try {
                // Assume that all the ratings are commited.
                nrAH.clearRatings();

                // Remove the commited messages from the storage.
                for (int lmID : postInfo.getCommited()) {
                    nmeAH.removeNewMessage(lmID);
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
        } catch (RsdnProcessorException e) {
            // Log the exception to console.
            trac.postException(e);
            return false;
        }
        return true;
    }
}