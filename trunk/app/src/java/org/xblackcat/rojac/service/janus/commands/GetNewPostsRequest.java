package org.xblackcat.rojac.service.janus.commands;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Version;
import org.xblackcat.rojac.data.VersionInfo;
import org.xblackcat.rojac.data.VersionType;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.janus.IJanusService;
import org.xblackcat.rojac.service.janus.JanusServiceException;
import org.xblackcat.rojac.service.janus.data.NewData;
import org.xblackcat.rojac.service.storage.IResult;
import org.xblackcat.rojac.service.storage.IVersionAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.service.storage.StorageException;
import ru.rsdn.Janus.RequestForumInfo;

import java.util.ArrayList;
import java.util.Collection;

import static org.xblackcat.rojac.service.options.Property.SYNCHRONIZER_LOAD_MESSAGES_PORTION;

/**
 * @author xBlackCat
 */

class GetNewPostsRequest extends LoadExtraMessagesRequest {
    private static final Log log = LogFactory.getLog(GetNewPostsRequest.class);

    protected int loadData(ILogTracker tracker, IJanusService janusService) throws StorageException, RsdnProcessorException {
        StringBuilder idsListBuilder = new StringBuilder();
        Collection<RequestForumInfo> forumInfos = new ArrayList<>();

        try (IResult<RequestForumInfo> forumInfo = forumAH.getSubscribedForums()) {
            for (RequestForumInfo rfi : forumInfo) {
                forumInfos.add(rfi);
                idsListBuilder.append(", ");
                idsListBuilder.append(rfi.getForumId());
            }
        }

        if (forumInfos.isEmpty()) {
            if (log.isWarnEnabled()) {
                log.warn("You should select at least one forum to start synchronization.");
            }
            return 0;
        }

        String idsList = idsListBuilder.substring(2);

        tracker.addLodMessage(Message.Synchronize_Command_Name_NewPosts, idsList);

        if (log.isDebugEnabled()) {
            log.debug("Load new messages for forums [id=" + idsList + "]");
        }

        Integer limit = SYNCHRONIZER_LOAD_MESSAGES_PORTION.get();

        tracker.addLodMessage(Message.Synchronize_Message_Portion, limit);

        IVersionAH vAH = Storage.get(IVersionAH.class);
        Version messagesVersion = getVersion(vAH, VersionType.MESSAGE_ROW_VERSION);
        Version moderatesVersion = getVersion(vAH, VersionType.MODERATE_ROW_VERSION);
        Version ratingsVersion = getVersion(vAH, VersionType.RATING_ROW_VERSION);

        int ownUserId = 0;
        int portionSize;
        do {
            if (ratingsVersion.isEmpty()) {
                ratingsVersion = moderatesVersion;
            }

            NewData data;
            try {
                data = janusService.getNewData(
                        forumInfos.toArray(new RequestForumInfo[forumInfos.size()]),
                        ratingsVersion,
                        messagesVersion,
                        moderatesVersion,
                        ArrayUtils.EMPTY_INT_ARRAY,
                        ArrayUtils.EMPTY_INT_ARRAY,
                        limit
                );
            } catch (JanusServiceException e) {
                throw new RsdnProcessorException("Can not load new portion of data", e);
            }

            if (ownUserId == 0) {
                ownUserId = data.getOwnUserId();
            }

            portionSize = data.getMessages().length;

            storeNewPosts(tracker, data);

            ratingsVersion = data.getRatingRowVersion();
            messagesVersion = data.getForumRowVersion();
            moderatesVersion = data.getModerateRowVersion();

            vAH.updateVersionInfo(new VersionInfo(messagesVersion, VersionType.MESSAGE_ROW_VERSION));
            vAH.updateVersionInfo(new VersionInfo(moderatesVersion, VersionType.MODERATE_ROW_VERSION));
            vAH.updateVersionInfo(new VersionInfo(ratingsVersion, VersionType.RATING_ROW_VERSION));

        } while (portionSize == limit);

        super.loadData(tracker, janusService);

        return ownUserId;
    }

    private static Version getVersion(IVersionAH vAH, VersionType type) throws StorageException {
        VersionInfo versionInfo = vAH.getVersionInfo(type);
        return versionInfo == null ? new Version() : versionInfo.getVersion();
    }
}
