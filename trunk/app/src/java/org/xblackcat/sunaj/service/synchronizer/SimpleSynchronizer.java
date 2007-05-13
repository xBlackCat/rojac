package org.xblackcat.sunaj.service.synchronizer;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.sunaj.service.data.*;
import org.xblackcat.sunaj.service.janus.IJanusService;
import org.xblackcat.sunaj.service.janus.JanusService;
import org.xblackcat.sunaj.service.janus.JanusServiceException;
import org.xblackcat.sunaj.service.janus.data.NewData;
import org.xblackcat.sunaj.service.janus.data.UsersList;
import org.xblackcat.sunaj.service.options.IOptionsService;
import org.xblackcat.sunaj.service.options.OptionsServiceFactory;
import org.xblackcat.sunaj.service.options.Property;
import org.xblackcat.sunaj.service.storage.*;

import java.util.HashSet;
import java.util.Set;

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

        try {
            if (os.getProperty(Property.SYNCHRONIZER_LOAD_USERS)) {
                loadUsers();
            }

            loadNewMessages();


            if (os.getProperty(Property.SYNCHRONIZER_LOAD_BROKEN_TOPICS_AT_ONCE)) {
                // TODO: Search and load broken topics.
            }
        } catch (SynchronizationException e) {
            // Log the exception to console.
            log.error("Synchronization failed.", e);
            throw e;
        }
    }

    private void loadNewMessages() throws SynchronizationException {
        IOptionsService os = OptionsServiceFactory.getOptionsService();
        IVersionDAO vDAO = storage.getVersionDAO();
        try {
//            int[] forumIds = storage.getForumDAO().getSubscribedForumIds();
            int[] forumIds = new int[]{33};
            if (ArrayUtils.isEmpty(forumIds)) {
                if (log.isWarnEnabled()) {
                    log.warn("You should select at least one forum to start synchronization.");
                }
                return;
            }

            Integer limit = os.getProperty(Property.SYNCHRONIZER_LOAD_MESSAGES_PORTION);

            Version messagesVersion = getVersion(VersionType.MESSAGE_ROW_VERSION);
            Version moderatesVersion = getVersion(VersionType.MODERATE_ROW_VERSION);
            Version ratingsVersion = getVersion(VersionType.RATING_ROW_VERSION);

            int[] brokenTopicIds = getBrokenTopicIds();
            int[] brokenMessageIds = new int[0];


            IRatingDAO rDAO = storage.getRatingDAO();
            IMessageDAO mDAO = storage.getMessageDAO();
            IModerateDAO modDAO = storage.getModerateDAO();

            Set<Integer> topics = new HashSet<Integer>();

            NewData data;
            do {
                boolean[] forumRequest = getForumsStatus(forumIds);

                data = js.getNewData(forumIds, forumRequest,
                        ratingsVersion, messagesVersion, moderatesVersion,
                        ArrayUtils.EMPTY_INT_ARRAY, ArrayUtils.EMPTY_INT_ARRAY,
                        limit);


                for (Message mes : data.getMessages()) {
                    mDAO.storeForumMessage(mes);
                    int topicId = mes.getTopicId();
                    if (topicId != 0) {
                        topics.add(topicId);
                    }
                }
                for (Moderate mod : data.getModerates()) {
                    modDAO.storeModerateInfo(mod);
                }
                for (Rating r : data.getRatings()) {
                    rDAO.storeRating(r);
                }

                ratingsVersion = data.getRatingRowVersion();
                messagesVersion = data.getForumRowVersion();
                moderatesVersion = data.getModerateRowVersion();

                vDAO.updateVersionInfo(new VersionInfo(messagesVersion, VersionType.MESSAGE_ROW_VERSION));
                vDAO.updateVersionInfo(new VersionInfo(moderatesVersion, VersionType.MODERATE_ROW_VERSION));
                vDAO.updateVersionInfo(new VersionInfo(ratingsVersion, VersionType.RATING_ROW_VERSION));

            } while (data.getMessages().length > 0);

            // TODO: load the broken topics and extra messages.

        } catch (StorageException e) {
            throw new SynchronizationException("Can not obtain local versions.", e);
        } catch (JanusServiceException e) {
            throw new SynchronizationException("Can not download the new messages", e);
        }
    }

    private boolean[] getForumsStatus(int[] forumIds) throws StorageException {
        IMessageDAO mDAO = storage.getMessageDAO();

        boolean[] status = new boolean[forumIds.length];
        for (int i = 0; i < forumIds.length; i++) {
            status[i] = !mDAO.isMessagesExistInForum(forumIds[i]);
        }
        return status;
    }

    private void loadUsers() throws SynchronizationException {
        IOptionsService os = OptionsServiceFactory.getOptionsService();
        IVersionDAO vDAO = storage.getVersionDAO();
        IUserDAO uDAO = storage.getUserDAO();

        if (log.isInfoEnabled()) {
            log.info("Loading new users information.");
        }
        Integer limit = os.getProperty(Property.SYNCHRONIZER_LOAD_USERS_PORTION);
        try {
            Version localUsersVersion = getVersion(VersionType.USERS_ROW_VERSION);

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

    private Version getVersion(VersionType type) throws StorageException {
        IVersionDAO vDAO = storage.getVersionDAO();
        VersionInfo versionInfo = vDAO.getVersionInfo(type);
        return versionInfo == null ? new Version() : versionInfo.getVersion();
    }

    /**
     * Looks for broken topics: the messages which has no parent in local database.
     *
     * @return array of the broken topic ids.
     */
    private int[] getBrokenTopicIds() {
        IMessageDAO mDAO = storage.getMessageDAO();

        return new int[0];
    }
}
