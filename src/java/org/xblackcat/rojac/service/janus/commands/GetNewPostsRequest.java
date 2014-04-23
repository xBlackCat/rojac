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
import org.xblackcat.rojac.service.storage.IVersionAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.sjpu.storage.StorageException;
import ru.rsdn.janus.RequestForumInfo;

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

        for (RequestForumInfo rfi : forumAH.getSubscribedForums()) {
                forumInfos.add(rfi);
                idsListBuilder.append(", ");
                idsListBuilder.append(rfi.getForumId());
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
                        forumInfos,
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

            portionSize = data.getMessages().size();

            storeNewPosts(tracker, data);

            ratingsVersion = data.getRatingRowVersion();
            messagesVersion = data.getForumRowVersion();
            moderatesVersion = data.getModerateRowVersion();

            final VersionInfo v = new VersionInfo(VersionType.MESSAGE_ROW_VERSION, messagesVersion);
            vAH.updateVersionInfo(v.getType(), v.getVersion());
            final VersionInfo v1 = new VersionInfo(VersionType.MODERATE_ROW_VERSION, moderatesVersion);
            vAH.updateVersionInfo(v1.getType(), v1.getVersion());
            final VersionInfo v2 = new VersionInfo(VersionType.RATING_ROW_VERSION, ratingsVersion);
            vAH.updateVersionInfo(v2.getType(), v2.getVersion());

        } while (portionSize == limit);

        super.loadData(tracker, janusService);

        return ownUserId;
    }

    private static Version getVersion(IVersionAH vAH, VersionType type) throws StorageException {
        VersionInfo versionInfo = vAH.getVersionInfo(type);
        return versionInfo == null ? new Version() : versionInfo.getVersion();
    }
}
