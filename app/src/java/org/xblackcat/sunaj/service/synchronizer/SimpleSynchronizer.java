package org.xblackcat.sunaj.service.synchronizer;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.sunaj.data.*;
import org.xblackcat.sunaj.service.ServiceFactory;
import org.xblackcat.sunaj.service.janus.IJanusService;
import org.xblackcat.sunaj.service.janus.JanusService;
import org.xblackcat.sunaj.service.janus.JanusServiceException;
import org.xblackcat.sunaj.service.janus.data.ForumsList;
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

    public void updateForumList() throws SynchronizationException {
        if (log.isInfoEnabled()) {
            log.info("Forum list synchronization started.");
        }
        IOptionsService os = ServiceFactory.getInstance().getOptionsService();

        IVersionAH vAH = storage.getVersionAH();

        VersionInfo vi;
        try {
            vi = vAH.getVersionInfo(VersionType.FORUM_ROW_VERSION);

            if (vi == null) {
                vi = new VersionInfo(new Version(), VersionType.FORUM_ROW_VERSION);
            }
        } catch (StorageException e) {
            throw new SynchronizationException("Can not obtain last forum version", e);
        }

        ForumsList forumsList;
        try {
            forumsList = janusService.getForumsList(vi.getVersion());
        } catch (JanusServiceException e) {
            throw new SynchronizationException("Can not obtain forums list", e);
        }

        IForumAH fAH = storage.getForumAH();
        IForumGroupAH gAH = storage.getForumGroupAH();

        try {
            for (ForumGroup fg : forumsList.getFourumGroups()) {
                if (gAH.getForumGroupById(fg.getForumGroupId()) == null) {
                    gAH.storeForumGroup(fg);
                } else {
                    gAH.updateForumGroup(fg);
                }
            }

            for (Forum f : forumsList.getForums()) {
                if (fAH.getForumById(f.getForumId()) == null) {
                    fAH.storeForum(f);
                } else {
                    fAH.updateForum(f);
                }
            }

            storage.getVersionAH().updateVersionInfo(new VersionInfo(forumsList.getVersion(), VersionType.FORUM_ROW_VERSION));
        } catch (StorageException e) {
            throw new SynchronizationException("Can not update forum list", e);
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

            loadExtraMessage();
        } catch (SynchronizationException e) {
            // Log the exception to console.
            log.error("Synchronization failed.", e);
            throw e;
        }
    }

    private void loadExtraMessage() throws SynchronizationException {
        int[] ids = ArrayUtils.EMPTY_INT_ARRAY;

        if (ArrayUtils.isEmpty(ids)) {
            return;
        }

        TopicMessages extra;
        try {
            extra = janusService.getTopicByMessage(ids);
        } catch (JanusServiceException e) {
            throw new SynchronizationException("Can not load extra messages.", e);
        }
        Message[] messages = extra.getMessages();
        Moderate[] moderates = extra.getModerates();
        Rating[] ratings = extra.getRatings();

        IMessageAH ah = storage.getMessageAH();
        try {
            for (Message m : messages) {
                ah.storeMessage(m);
            }
        } catch (StorageException e) {
            throw new SynchronizationException("Can not store extra messages into storage", e);
        }

        IModerateAH moderateAH = storage.getModerateAH();

    }

    private void postChanges() throws SynchronizationException {
        INewRatingAH nrAH = storage.getNewRatingAH();
        INewMessageAH nmeAH = storage.getNewMessageAH();
        INewModerateAH nmoAH = storage.getNewModerateAH();

        NewRating[] newRatings;
        NewMessage[] newMessages;
        NewModerate[] newModerates;
        try {
            newRatings = nrAH.getAllNewRatings();
            newMessages = nmeAH.getAllNewMessages();
            newModerates = nmoAH.getAllNewModerates();
        } catch (StorageException e) {
            throw new SynchronizationException("Can not load your changes.", e);
        }

        if (ArrayUtils.isEmpty(newRatings) &&
                ArrayUtils.isEmpty(newMessages) &&
                ArrayUtils.isEmpty(newModerates)) {

            if (log.isDebugEnabled()) {
                log.debug("Nothing to post.");
            }
            return;
        }

        PostInfo postInfo;
        try {
            janusService.postChanges(newMessages, newRatings, newModerates);
            postInfo = janusService.commitChanges();
        } catch (JanusServiceException e) {
            throw new SynchronizationException("Can not post your changes to the RSDN.", e);
        }

        try {
            // Assume that all the ratings are commited.
            nrAH.clearRatings();

            // Remove the commited messages from the storage.
            for (int lmID : postInfo.getCommited()) {
                nmeAH.removeNewMessage(lmID);
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
        IVersionAH vAH = storage.getVersionAH();
        try {
            int[] forumIds = storage.getForumAH().getSubscribedForumIds();
            if (ArrayUtils.isEmpty(forumIds)) {
                if (log.isWarnEnabled()) {
                    log.warn("You should select at least one forum to start synchronization.");
                }
                return;
            }

            if (log.isDebugEnabled()) {
                log.debug("Load new messages for forums [id=" + ArrayUtils.toString(forumIds) + "]");
            }

            Integer limit = os.getProperty(Property.SYNCHRONIZER_LOAD_MESSAGES_PORTION);

            Version messagesVersion = getVersion(VersionType.MESSAGE_ROW_VERSION);
            Version moderatesVersion = getVersion(VersionType.MODERATE_ROW_VERSION);
            Version ratingsVersion = getVersion(VersionType.RATING_ROW_VERSION);

            IRatingAH rAH = storage.getRatingAH();
            IMessageAH mAH = storage.getMessageAH();
            IModerateAH modAH = storage.getModerateAH();

            // Broken topic ids
//            TIntHashSet topics = new TIntHashSet();

            NewData data;
            do {
                data = janusService.getNewData(
                        forumIds,
                        messagesVersion.getBytes().length == 0,
                        ratingsVersion,
                        messagesVersion,
                        moderatesVersion,
                        ArrayUtils.EMPTY_INT_ARRAY,
                        ArrayUtils.EMPTY_INT_ARRAY,
                        limit
                );


                for (Message mes : data.getMessages()) {
                    if (mAH.getMessageById(mes.getMessageId()) != null) {
                        mAH.updateMessage(mes);
                    } else {
                        mAH.storeMessage(mes);
                    }
/*
                    int topicId = mes.getTopicId();
                    if (topicId != 0) {
                        topics.add(topicId);
                    }
*/
                }
                for (Moderate mod : data.getModerates()) {
                    modAH.storeModerateInfo(mod);
                }
                for (Rating r : data.getRatings()) {
                    rAH.storeRating(r);
                }

                ratingsVersion = data.getRatingRowVersion();
                messagesVersion = data.getForumRowVersion();
                moderatesVersion = data.getModerateRowVersion();

                vAH.updateVersionInfo(new VersionInfo(messagesVersion, VersionType.MESSAGE_ROW_VERSION));
                vAH.updateVersionInfo(new VersionInfo(moderatesVersion, VersionType.MODERATE_ROW_VERSION));
                vAH.updateVersionInfo(new VersionInfo(ratingsVersion, VersionType.RATING_ROW_VERSION));

            } while (data.getMessages().length > 0);

            // remove existing ids from downloaded topic ids.
/*            int[] messageIds = topics.toArray();
            for (int mId : messageIds) {
                if (mAH.getMessageById(mId) != null) {
                    topics.remove(mId);
                }
            }

            if (log.isInfoEnabled()) {
                log.info("Found broken topics: " + ArrayUtils.toString(topics.toArray()));
            }

            IMiscAH eAH = storage.getMiscAH();

            topics.addAll(eAH.getExtraMessages());

            TopicMessages fullTopics = janusService.getTopicByMessage(topics.toArray());
            for (Message mes : fullTopics.getMessages()) {
                int mId = mes.getMessageId();
                if (mAH.getMessageById(mId) == null) {
                    mAH.storeMessage(mes);
                } else {
                    mAH.updateMessage(mes);
                }
                modAH.removeModerateInfosByMessageId(mId);
                rAH.removeRatingsByMessageId(mId);
            }
            for (Moderate mod : fullTopics.getModerates()) {
                modAH.storeModerateInfo(mod);
            }
            for (Rating r : fullTopics.getRatings()) {
                rAH.storeRating(r);
            }

            eAH.clearExtraMessages();
*/
        } catch (StorageException e) {
            throw new SynchronizationException("Can not obtain local versions.", e);
        } catch (JanusServiceException e) {
            throw new SynchronizationException("Can not download the new messages", e);
        }
    }

    private void loadUsers() throws SynchronizationException {
        IOptionsService os = ServiceFactory.getInstance().getOptionsService();
        IVersionAH vAH = storage.getVersionAH();
        IUserAH uAH = storage.getUserAH();

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
                vAH.updateVersionInfo(new VersionInfo(localUsersVersion, VersionType.USERS_ROW_VERSION));
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
        IVersionAH vAH = storage.getVersionAH();
        VersionInfo versionInfo = vAH.getVersionInfo(type);
        return versionInfo == null ? new Version() : versionInfo.getVersion();
    }

}
