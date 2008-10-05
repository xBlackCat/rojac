package org.xblackcat.rojac.service.synchronizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.User;
import org.xblackcat.rojac.data.Version;
import org.xblackcat.rojac.data.VersionType;
import org.xblackcat.rojac.gui.frame.progress.IProgressTracker;
import org.xblackcat.rojac.service.janus.JanusServiceException;
import org.xblackcat.rojac.service.janus.data.UsersList;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IUserAH;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.RojacException;

/**
 * Date: 27 вер 2008
 *
 * @author xBlackCat
 */

public class LoadUsersCommand extends ARsdnCommand<Boolean> {
    private static final Log log = LogFactory.getLog(LoadUsersCommand.class);

    public LoadUsersCommand(IResultHandler<Boolean> booleanIResultHandler) {
        super(booleanIResultHandler);
    }

    public Boolean process(IProgressTracker trac) throws RojacException {
        IUserAH uAH = storage.getUserAH();

        if (log.isInfoEnabled()) {
            log.info("Loading new users information.");
        }
        Integer limit = optionsService.getProperty(Property.SYNCHRONIZER_LOAD_USERS_PORTION);
        try {
            Version localUsersVersion = getVersion(VersionType.USERS_ROW_VERSION);

            int totalUsersNumber = 0;

            UsersList users;
            do {                                                               
                if (log.isDebugEnabled()) {
                    log.debug("Load next portion of the new users. Portion limit = " + limit + " (" + totalUsersNumber + " already loaded).");
                }
                users = janusService.getNewUsers(localUsersVersion, limit);
                totalUsersNumber += users.getUsers().length;
                for (User user : users.getUsers()) {
                    if (log.isTraceEnabled()) {
                        log.trace("Store the " + user + " in the storage.");
                    }
                    uAH.storeUser(user);
                }
                localUsersVersion = users.getVersion();
                setVersion(VersionType.USERS_ROW_VERSION, localUsersVersion);
            } while (users.getUsers().length > 0);

            if (log.isInfoEnabled()) {
                log.info(totalUsersNumber + " user(s) was loaded.");
            }
        } catch (StorageException e) {
            throw new SynchronizationException("Can not get the local users version", e);
        } catch (JanusServiceException e) {
            throw new SynchronizationException("Can not get the users list from the RSDN server.", e);
        }
        return true;
    }
}
