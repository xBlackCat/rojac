package org.xblackcat.sunaj.service.synchronizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.sunaj.service.data.User;
import org.xblackcat.sunaj.service.data.Version;
import org.xblackcat.sunaj.service.data.VersionInfo;
import org.xblackcat.sunaj.service.data.VersionType;
import org.xblackcat.sunaj.service.janus.IJanusService;
import org.xblackcat.sunaj.service.janus.JanusService;
import org.xblackcat.sunaj.service.janus.JanusServiceException;
import org.xblackcat.sunaj.service.janus.data.UsersList;
import org.xblackcat.sunaj.service.options.IOptionsService;
import org.xblackcat.sunaj.service.options.OptionsServiceFactory;
import org.xblackcat.sunaj.service.options.Property;
import org.xblackcat.sunaj.service.storage.IStorage;
import org.xblackcat.sunaj.service.storage.IUserDAO;
import org.xblackcat.sunaj.service.storage.IVersionDAO;
import org.xblackcat.sunaj.service.storage.StorageException;

/**
 * Date: 12 трав 2007
 *
 * @author ASUS
 */

public class SimpleSynchronizer implements ISynchronizer {
    private static final Log log = LogFactory.getLog(SimpleSynchronizer.class);

    private final IJanusService js;
    private final IStorage storage;

    public SimpleSynchronizer(String login, String password, IStorage storage) throws SynchronizationException {
        this.storage = storage;
        try {
            js = JanusService.getInstance(login, password);
        } catch (JanusServiceException e) {
            throw new SynchronizationException("Can not initialize the synchronizator.", e);
        }
    }

    public void synchronize() throws SynchronizationException {
        if (log.isInfoEnabled()) {
            log.info("Synchronization started.");
        }
        IOptionsService os = OptionsServiceFactory.getOptionsService();
        IVersionDAO vDAO = storage.getVersionDAO();


        try {
            if (os.getProperty(Property.SYNCHRONIZER_LOAD_USERS)) {
                IUserDAO uDAO = storage.getUserDAO();

                if (log.isInfoEnabled()) {
                    log.info("Loading new users information.");
                }
                Integer limit = os.getProperty(Property.SYNCHRONIZER_LOAD_USERS_LIMIT);
                try {
                    VersionInfo versionInfo = vDAO.getVersionInfo(VersionType.USERS_ROW_VERSION);
                    Version localUsersVersion = versionInfo == null ? new Version() : versionInfo.getVersion();

                    int totalUsersNumber = 0;

                    UsersList users;
                    do {
                        if (log.isDebugEnabled()) {
                            log.debug("Load next portion of the new users. Portion limit = " + limit);
                        }
                        users = js.getNewUsers(localUsersVersion, limit);
                        totalUsersNumber += users.getUsers().length;
                        for (User user : users.getUsers()) {
                            if (log.isDebugEnabled()) {
                                log.debug("Store the " + user + " in the storage.");
                            }
                            uDAO.storeUser(user);
                        }
                        localUsersVersion = users.getVersion();
                        vDAO.updateVersionInfo(new VersionInfo(localUsersVersion, VersionType.USERS_ROW_VERSION));
                    } while (users.getUsers().length > 0);

                    if (log.isInfoEnabled()) {
                        log.info(totalUsersNumber + " user(s) was loaded.");
                    }
                } catch (StorageException e) {
                    throw new SynchronizationException("Can not get the local users version", e);
                } catch (JanusServiceException e) {
                    throw new SynchronizationException("Can not get the users list from the RSDN server.", e);
                }
            }

            // TODO: load new messages

            if (os.getProperty(Property.SYNCHRONIZER_LOAD_BROKEN_TOPICS_AT_ONCE)) {
                // TODO: Search and load broken topics.
            }
        } catch (SynchronizationException e) {
            // Log the exception to console.
            log.error("Synchronization failed.", e);
            throw e;
        }
    }
}
