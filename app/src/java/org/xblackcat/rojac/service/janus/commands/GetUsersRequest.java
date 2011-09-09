package org.xblackcat.rojac.service.janus.commands;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.data.User;
import org.xblackcat.rojac.data.Version;
import org.xblackcat.rojac.data.VersionType;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.janus.IJanusService;
import org.xblackcat.rojac.service.janus.JanusServiceException;
import org.xblackcat.rojac.service.janus.data.UsersList;
import org.xblackcat.rojac.service.storage.IUserAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.service.storage.StorageException;

import static org.xblackcat.rojac.service.options.Property.SYNCHRONIZER_LOAD_USERS_PORTION;

/**
 * @author xBlackCat
 */

class GetUsersRequest extends ARequest<IPacket> {
    private static final Log log = LogFactory.getLog(GetUsersRequest.class);

    public void process(IResultHandler<IPacket> handler, IProgressTracker tracker, IJanusService janusService) throws RojacException {
        IUserAH uAH = Storage.get(IUserAH.class);

        tracker.addLodMessage(Message.Synchronize_Command_Name_Users);
        if (log.isDebugEnabled()) {
            log.debug("Loading new users information.");
        }
        Integer limit = SYNCHRONIZER_LOAD_USERS_PORTION.get();
        tracker.addLodMessage(Message.Synchronize_Message_Portion, limit);

        try {
            Version localUsersVersion = DataHelper.getVersion(VersionType.USERS_ROW_VERSION);

            int totalUsersNumber = 0;

            UsersList users;
            int loaded;
            do {
                if (log.isDebugEnabled()) {
                    log.debug("Load next portion of the new users. Portion limit = " + limit + " (" + totalUsersNumber + " already loaded).");
                }
                users = janusService.getNewUsers(localUsersVersion, limit);
                loaded = users.getUsers().length;
                totalUsersNumber += loaded;

                int count = 0;
                for (User user : users.getUsers()) {
                    if (log.isTraceEnabled()) {
                        log.trace("Store the " + user + " in the storage.");
                    }
                    if (uAH.getUserById(user.getId()) == null) {
                        uAH.storeUser(user);
                    } else {
                        uAH.updateUser(user);
                    }
                    tracker.updateProgress(count++, loaded);
                }
                localUsersVersion = users.getVersion();
                DataHelper.setVersion(VersionType.USERS_ROW_VERSION, localUsersVersion);
            } while (loaded > 0);

            tracker.addLodMessage(Message.Synchronize_Message_GotUsers, totalUsersNumber);
            if (log.isDebugEnabled()) {
                log.debug(totalUsersNumber + " user(s) was loaded.");
            }

        } catch (StorageException e) {
            throw new RsdnProcessorException("Can not get the local users version", e);
        } catch (JanusServiceException e) {
            throw new RsdnProcessorException("Can not get the users list from the RSDN server.", e);
        }
    }
}
