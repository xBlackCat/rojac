package org.xblackcat.rojac.service.janus.commands;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.data.User;
import org.xblackcat.rojac.data.Version;
import org.xblackcat.rojac.data.VersionInfo;
import org.xblackcat.rojac.data.VersionType;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.janus.IJanusService;
import org.xblackcat.rojac.service.janus.JanusServiceException;
import org.xblackcat.rojac.service.janus.data.UsersList;
import org.xblackcat.rojac.service.storage.IUserAH;
import org.xblackcat.rojac.service.storage.IVersionAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.sjpu.storage.StorageException;

import static org.xblackcat.rojac.service.options.Property.SYNCHRONIZER_LOAD_USERS_PORTION;

/**
 * @author xBlackCat
 */

class GetUsersRequest extends ARequest<IPacket> {
    private static final Log log = LogFactory.getLog(GetUsersRequest.class);

    public void process(IResultHandler<IPacket> handler, ILogTracker tracker, IJanusService janusService) throws RojacException {
        IUserAH uAH = Storage.get(IUserAH.class);

        tracker.addLodMessage(Message.Synchronize_Command_Name_Users);
        if (log.isDebugEnabled()) {
            log.debug("Loading new users information.");
        }
        Integer limit = SYNCHRONIZER_LOAD_USERS_PORTION.get();
        tracker.addLodMessage(Message.Synchronize_Message_Portion, limit);

        try {
            IVersionAH vAH1 = Storage.get(IVersionAH.class);
            VersionInfo versionInfo = vAH1.getVersionInfo(VersionType.USERS_ROW_VERSION);
            Version localUsersVersion = versionInfo == null ? new Version() : versionInfo.getVersion();

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
                        uAH.storeUser(
                                user.getId(), user.getUserName(), user.getUserNick(), user.getRealName(), user.getPublicEmail(),
                                      user.getHomePage(), user.getSpecialization(), user.getWhereFrom(), user.getOrigin()
                        );
                    } else {
                        uAH.updateUser(
                                user.getUserName(), user.getUserNick(), user.getRealName(), user.getPublicEmail(),
                                       user.getHomePage(), user.getSpecialization(), user.getWhereFrom(), user.getOrigin(), user.getId()
                        );
                    }
                    tracker.updateProgress(count++, loaded);
                }
                localUsersVersion = users.getVersion();
                IVersionAH vAH = Storage.get(IVersionAH.class);
                final VersionInfo v = new VersionInfo(VersionType.USERS_ROW_VERSION, localUsersVersion);
                vAH.updateVersionInfo(v.getType(), v.getVersion());
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
