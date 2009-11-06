package org.xblackcat.rojac.service.commands;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.data.User;
import org.xblackcat.rojac.data.Version;
import org.xblackcat.rojac.data.VersionType;
import org.xblackcat.rojac.gui.dialogs.progress.IProgressTracker;
import org.xblackcat.rojac.service.janus.JanusServiceException;
import org.xblackcat.rojac.service.janus.data.UsersList;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IUserAH;
import org.xblackcat.rojac.service.storage.StorageException;

/**
 * @author xBlackCat
 */

public class LoadUsersCommand extends ARsdnCommand<AffectedPosts> {
    private static final Log log = LogFactory.getLog(LoadUsersCommand.class);

    public LoadUsersCommand(IResultHandler<AffectedPosts> booleanIResultHandler) {
        super(booleanIResultHandler);
    }

    public AffectedPosts process(IProgressTracker trac) throws RojacException {
        IUserAH uAH = storage.getUserAH();

        if (log.isInfoEnabled()) {
            log.info("Loading new users information.");
        }
        Integer limit = Property.SYNCHRONIZER_LOAD_USERS_PORTION.get();
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
            throw new RsdnProcessorException("Can not get the local users version", e);
        } catch (JanusServiceException e) {
            throw new RsdnProcessorException("Can not get the users list from the RSDN server.", e);
        }
        return new AffectedPosts();
    }
}
