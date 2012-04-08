package org.xblackcat.rojac.service.janus.commands;

import gnu.trove.map.hash.TIntObjectHashMap;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.NewMessage;
import org.xblackcat.rojac.data.NewModerate;
import org.xblackcat.rojac.data.NewRating;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.janus.IJanusService;
import org.xblackcat.rojac.service.janus.JanusServiceException;
import org.xblackcat.rojac.service.janus.data.PostException;
import org.xblackcat.rojac.service.janus.data.PostInfo;
import org.xblackcat.rojac.service.storage.*;
import org.xblackcat.rojac.util.SynchronizationUtils;

import java.util.Collection;

/**
 * @author xBlackCat
 */

class PostChangesRequest extends ARequest<IPacket> {
    private static final Log log = LogFactory.getLog(PostChangesRequest.class);

    public void process(IResultHandler<IPacket> handler, ILogTracker trac, IJanusService janusService) {
        trac.addLodMessage(Message.Synchronize_Command_Name_Submit);

        try {
            INewRatingAH nrAH = Storage.get(INewRatingAH.class);
            INewMessageAH nmeAH = Storage.get(INewMessageAH.class);
            INewModerateAH nmoAH = Storage.get(INewModerateAH.class);

            Collection<NewRating> newRatings;
            Collection<NewMessage> newMessages;
            Collection<NewModerate> newModerates;
            try {
                newRatings = SynchronizationUtils.collect(nrAH.getAllNewRatings());
                newMessages = SynchronizationUtils.collect(nmeAH.getNewMessagesToSend());
                newModerates = SynchronizationUtils.collect(nmoAH.getAllNewModerates());
            } catch (StorageException e) {
                throw new RsdnProcessorException("Can not load your changes.", e);
            }

            if (CollectionUtils.isEmpty(newRatings) &&
                    CollectionUtils.isEmpty(newMessages) &&
                    CollectionUtils.isEmpty(newModerates)) {

                if (log.isDebugEnabled()) {
                    log.debug("Nothing to post.");
                }
                return;
            }

            // Store forum ids of new messages and message ids of new ratings to return update event
            TIntObjectHashMap<NewMessage> messageForumIds = new TIntObjectHashMap<>();

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

            try {
                // Assume that all the ratings are commited.
                nrAH.clearRatings();

                // Remove the commited messages from the storage.
                for (int lmID : postInfo.getCommited()) {
                    nmeAH.removeNewMessage(lmID);
                    NewMessage newMessage = messageForumIds.get(lmID);
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

            // TODO: add notification if non-sent messages is shown in threads
        } catch (RsdnProcessorException e) {
            // Log the exception to console.
            trac.postException(e);
        }
    }
}
