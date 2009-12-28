package org.xblackcat.rojac.service.janus.commands;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.data.User;
import org.xblackcat.rojac.data.Version;
import org.xblackcat.rojac.data.VersionType;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.RojacHelper;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.janus.IJanusService;
import org.xblackcat.rojac.service.janus.JanusServiceException;
import org.xblackcat.rojac.service.janus.data.UsersList;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.IUserAH;
import org.xblackcat.rojac.service.storage.StorageException;

import static org.xblackcat.rojac.service.options.Property.SYNCHRONIZER_LOAD_USERS_PORTION;

/**
 * @author xBlackCat
 */

class GetUsersRequest extends ARequest {
    private static final Log log = LogFactory.getLog(GetUsersRequest.class);

    public AffectedIds process(IProgressTracker tracker, IJanusService janusService) throws RojacException {
        IStorage storage = ServiceFactory.getInstance().getStorage();
        IUserAH uAH = storage.getUserAH();

        tracker.addLodMessage(Messages.SYNCHRONIZE_COMMAND_NAME_USERS);
        if (log.isDebugEnabled()) {
            log.debug("Loading new users information.");
        }
        Integer limit = SYNCHRONIZER_LOAD_USERS_PORTION.get();
        tracker.addLodMessage(Messages.SYNCHRONIZE_COMMAND_PORTION, limit);

        try {
            Version localUsersVersion = RojacHelper.getVersion(VersionType.USERS_ROW_VERSION);

            int totalUsersNumber = 0;

            UsersList users;
            do {
                if (log.isDebugEnabled()) {
                    log.debug("Load next portion of the new users. Portion limit = " + limit + " (" + totalUsersNumber + " already loaded).");
                }
                users = janusService.getNewUsers(localUsersVersion, limit);
                totalUsersNumber += users.getUsers().length;

                int count = 0;
                for (User user : users.getUsers()) {
                    tracker.updateProgress(count++, users.getUsers().length);
                    if (log.isTraceEnabled()) {
                        log.trace("Store the " + user + " in the storage.");
                    }
                    uAH.storeUser(user);
                }
                localUsersVersion = users.getVersion();
                RojacHelper.setVersion(VersionType.USERS_ROW_VERSION, localUsersVersion);
            } while (users.getUsers().length > 0);

            tracker.addLodMessage(Messages.SYNCHRONIZE_COMMAND_GOT_USERS, totalUsersNumber);
            if (log.isDebugEnabled()) {
                log.debug(totalUsersNumber + " user(s) was loaded.");
            }

        } catch (StorageException e) {
            throw new RsdnProcessorException("Can not get the local users version", e);
        } catch (JanusServiceException e) {
            throw new RsdnProcessorException("Can not get the users list from the RSDN server.", e);
        }
        return new AffectedIds();
    }
}