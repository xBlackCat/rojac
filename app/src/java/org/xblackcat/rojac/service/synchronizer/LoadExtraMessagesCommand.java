package org.xblackcat.rojac.service.synchronizer;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Message;
import org.xblackcat.rojac.data.Moderate;
import org.xblackcat.rojac.data.Rating;
import org.xblackcat.rojac.gui.frame.progress.IProgressTracker;
import org.xblackcat.rojac.service.janus.JanusServiceException;
import org.xblackcat.rojac.service.janus.data.TopicMessages;
import org.xblackcat.rojac.service.storage.IMessageAH;
import org.xblackcat.rojac.service.storage.IModerateAH;
import org.xblackcat.rojac.service.storage.StorageException;

/**
 * Date: 26 вер 2008
 *
 * @author xBlackCat
 */

public class LoadExtraMessagesCommand extends ARsdnCommand {
    private static final Log log = LogFactory.getLog(LoadExtraMessagesCommand.class);

    private final int[] messageIds;

    public LoadExtraMessagesCommand(int[] messageIds) {
        this.messageIds = messageIds;
    }

    public void doTask(IProgressTracker trac) throws Exception {
        trac.addLodMessage("Synchronization started.");
        
        try {
            loadExtraMessage(messageIds);
        } catch (SynchronizationException e) {
            // Log the exception to console.
            trac.postException(e);
        }
    }

    protected void loadExtraMessage(int[] ids) throws SynchronizationException {
        if (ArrayUtils.isEmpty(ids)) {
            return;
        }

        TopicMessages extra;
        try {
            extra = janusService.getTopicByMessage(ids);
        } catch (JanusServiceException e) {
            throw new SynchronizationException("Can not load extra messages.", e);
        }
        Message[] messages = extra.getMessages();
        Moderate[] moderates = extra.getModerates();
        Rating[] ratings = extra.getRatings();

        IMessageAH ah = storage.getMessageAH();
        try {
            for (Message m : messages) {
                if (ah.isExist(m.getMessageId())) {
                    ah.updateMessage(m);
                } else {
                    ah.storeMessage(m);
                }
            }
        } catch (StorageException e) {
            throw new SynchronizationException("Can not store extra messages into storage", e);
        }

        IModerateAH moderateAH = storage.getModerateAH();

    }
}