package org.xblackcat.sunaj.service.synchronizer;

import gnu.trove.TIntHashSet;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.sunaj.data.*;
import org.xblackcat.sunaj.service.ServiceFactory;
import org.xblackcat.sunaj.service.janus.IJanusService;
import org.xblackcat.sunaj.service.janus.JanusService;
import org.xblackcat.sunaj.service.janus.JanusServiceException;
import org.xblackcat.sunaj.service.janus.data.NewData;
import org.xblackcat.sunaj.service.janus.data.PostException;
import org.xblackcat.sunaj.service.janus.data.PostInfo;
import org.xblackcat.sunaj.service.janus.data.TopicMessages;
import org.xblackcat.sunaj.service.janus.data.UsersList;
import org.xblackcat.sunaj.service.options.IOptionsService;
import org.xblackcat.sunaj.service.options.Property;
import org.xblackcat.sunaj.service.storage.*;

/**
 * Date: 12 трав 2007
 *
 * @author ASUS
 */

public class SimpleSynchronizer implements ISynchronizer {
    private static final Log log = LogFactory.getLog(SimpleSynchronizer.class);

    private final IJanusService janusService;
    private final IStorage storage;

    public SimpleSynchronizer(String login, String password, IStorage storage) throws SynchronizationException {
        this.storage = storage;
        try {
            janusService = JanusService.getInstance(login, password);
        } catch (JanusServiceException e) {
            throw new SynchronizationException("Can not initialize the synchronizator.", e);
        }
    }

    public void synchronize() throws SynchronizationException {
        if (log.isInfoEnabled()) {
            log.info("Synchronization started.");
        }
        IOptionsService os = ServiceFactory.getInstance().getOptionsService();

        try {
            postChanges();

            if (os.getProperty(Property.SYNCHRONIZER_LOAD_USERS)) {
                loadUsers();
            }

            loadNewMessages();
        } catch (SynchronizationException e) {
            // Log the exception to console.
            log.error("Synchronization failed.", e);
            throw e;
        }
    }

    private void postChanges() throws SynchronizationException {
        INewRatingAH nrDAO = storage.getNewRatingAH();
        INewMessageAH nmDAO = storage.getNewMessageAH();

        NewRating[] newRatings;
        NewMessage[] newMessages;
        try {
            newRatings = nrDAO.getAllNewRatings();
            newMessages = nmDAO.getAllNewMessages();
        } catch (StorageException e) {
            throw new SynchronizationException("Can not load your changes.", e);
        }

        if (ArrayUtils.isEmpty(newRatings) && ArrayUtils.isEmpty(newMessages)) {
            if (log.isDebugEnabled()) {
                log.debug("Nothing to post.");
            }
            return;
        }

        PostInfo postInfo;
        try {
            janusService.postChanges(newMessages, newRatings);
            postInfo = janusService.commitChanges();
        } catch (JanusServiceException e) {
            throw new SynchronizationException("Can not post your changes to the RSDN.", e);
        }

        try {
            // Assume that all the ratings are commited.
            nrDAO.clearRatings();

            // Remove the commited messages from the storage.
            for (int lmID : postInfo.getCommited()) {
                nmDAO.removeNewMessage(lmID);
            }

            // Show all the PostExceptions if any
            for (PostException pe : postInfo.getExceptions()) {
                if (log.isWarnEnabled()) {
                    log.warn(pe);
                }
            }
        } catch (StorageException e) {
            throw new SynchronizationException("Unable to process the commit response.", e);
        }
    }

    private void loadNewMessages() throws SynchronizationException {
        IOptionsService os = ServiceFactory.getInstance().getOptionsService();
        IVersionAH vDAO = storage.getVersionAH();
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

            IRatingAH rDAO = storage.getRatingAH();
            IMessageAH mDAO = storage.getMessageAH();
            IModerateAH modDAO = storage.getModerateAH();

            TIntHashSet topics = new TIntHashSet();

            NewData data;
            do {
                boolean s = messagesVersion.getBytes().length == 0;

                boolean[] status = new boolean[forumIds.length];
                for (int i = 0; i < forumIds.length; i++) {
                    status[i] = s;
                }

                data = janusService.getNewData(forumIds, status,
                        ratingsVersion, messagesVersion, moderatesVersion,
                        ArrayUtils.EMPTY_INT_ARRAY, ArrayUtils.EMPTY_INT_ARRAY,
                        limit);


                for (Message mes : data.getMessages()) {
                    mDAO.storeMessage(mes);
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

            // remove existing ids from downloaded topic ids.
            int[] messageIds = topics.toArray();
            for (int mId : messageIds) {
                if (mDAO.getMessageById(mId) != null) {
                    topics.remove(mId);
                }
            }

            if (log.isInfoEnabled()) {
                log.info("Found broken topics: " + ArrayUtils.toString(topics.toArray()));
            }

            IMiscAH eDAO = storage.getMiscAH();

            topics.addAll(eDAO.getExtraMessages());

            TopicMessages fullTopics = janusService.getTopicByMessage(topics.toArray());
            for (Message mes : fullTopics.getMessages()) {
                int mId = mes.getMessageId();
                if (mDAO.getMessageById(mId) == null) {
                    mDAO.storeMessage(mes);
                } else {
                    mDAO.updateMessage(mes);
                }
                modDAO.removeModerateInfosByMessageId(mId);
                rDAO.removeRatingsByMessageId(mId);
            }
            for (Moderate mod : fullTopics.getModerates()) {
                modDAO.storeModerateInfo(mod);
            }
            for (Rating r : fullTopics.getRatings()) {
                rDAO.storeRating(r);
            }

            eDAO.clearExtraMessages();
        } catch (StorageException e) {
            throw new SynchronizationException("Can not obtain local versions.", e);
        } catch (JanusServiceException e) {
            throw new SynchronizationException("Can not download the new messages", e);
        }
    }

    private void loadUsers() throws SynchronizationException {
        IOptionsService os = ServiceFactory.getInstance().getOptionsService();
        IVersionAH vDAO = storage.getVersionAH();
        IUserAH uDAO = storage.getUserAH();

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
                users = janusService.getNewUsers(localUsersVersion, limit);
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
        IVersionAH vDAO = storage.getVersionAH();
        VersionInfo versionInfo = vDAO.getVersionInfo(type);
        return versionInfo == null ? new Version() : versionInfo.getVersion();
    }

}
