package org.xblackcat.sunaj.service.storage;

import org.xblackcat.sunaj.service.data.*;


/**
 * Date: 15.04.2007
 *
 * @author ASUS
 */
public class CachedStorage implements IStorage {
    /*
     * Caches
     */
    private final Cache<Forum> forumCache = new Cache<Forum>();
    private final Cache<ForumGroup> forumGroupCache = new Cache<ForumGroup>();
    private final Cache<ForumMessage> messageCache = new Cache<ForumMessage>();
    private final Cache<ModerateInfo> moderateCache = new Cache<ModerateInfo>();
    private final Cache<Rating> ratingCache = new Cache<Rating>();
    private final Cache<User> userCache = new Cache<User>();

    private final IStorage storage;

    public CachedStorage(IStorage storage) {
        this.storage = storage;
    }

    public boolean checkStructure() throws StorageException {
        return storage.checkStructure();
    }

    public void initialize() throws StorageException {
        storage.initialize();
    }

    public void storeForum(Forum f) throws StorageException {
        storage.storeForum(f);
    }

    public void storeForumGroup(ForumGroup fg) throws StorageException {
        storage.storeForumGroup(fg);
    }

    public void storeForumMessage(ForumMessage fm) throws StorageException {
        storage.storeForumMessage(fm);
    }

    public void storeModerateInfo(ModerateInfo mi) throws StorageException {
        storage.storeModerateInfo(mi);
    }

    public void storeNewMessage(NewMessage nm) throws StorageException {
        storage.storeNewMessage(nm);
    }

    public void storeNewRating(NewRating nr) throws StorageException {
        storage.storeNewRating(nr);
    }

    public void storeRating(Rating ri) throws StorageException {
        storage.storeRating(ri);
    }

    public void storeUser(User ui) throws StorageException {
        storage.storeUser(ui);
    }

    public boolean removeForum(int id) throws StorageException {
        if (storage.removeForum(id)) {
            forumCache.removeObject(id);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeForumGroup(int id) throws StorageException {
        if (storage.removeForumGroup(id)) {
            forumGroupCache.removeObject(id);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeForumMessage(int id) throws StorageException {
        if (storage.removeForumMessage(id)) {
            messageCache.removeObject(id);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeModerateInfo(int id) throws StorageException {
        if (storage.removeModerateInfo(id)) {
            moderateCache.removeObject(id);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeNewMessage(int id) throws StorageException {
        return storage.removeNewMessage(id);
    }

    public boolean removeNewRating(int id) throws StorageException {
        return storage.removeNewRating(id);
    }

    public boolean removeRating(int id) throws StorageException {
        if (storage.removeRating(id)) {
            ratingCache.removeObject(id);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeUser(int id) throws StorageException {
        if (storage.removeUser(id)) {
            userCache.removeObject(id);
            return true;
        } else {
            return false;
        }
    }

    public Forum getForumById(int forumId) throws StorageException {
        Forum forum = forumCache.getObject(forumId);
        if (forum == null) {
            forum = storage.getForumById(forumId);
            if (forum != null) {
                forumCache.putObject(forumId, forum);
            }
        }
        return forum;
    }

    public int[] getForumIdsInGroup(int forumGroupId) throws StorageException {
        return storage.getForumIdsInGroup(forumGroupId);
    }

    public int[] getAllForumIds() throws StorageException {
        return storage.getAllForumIds();
    }

    public ForumGroup getForumGroupById(int forumGroupId) throws StorageException {
        ForumGroup forumGroup = forumGroupCache.getObject(forumGroupId);
        if (forumGroup == null) {
            forumGroup = storage.getForumGroupById(forumGroupId);
            if (forumGroup != null) {
                forumGroupCache.putObject(forumGroupId, forumGroup);
            }
        }
        return forumGroup;
    }

    public int[] getAllForumGroupIds() throws StorageException {
        return storage.getAllForumGroupIds();
    }

    public ForumMessage getMessageById(int messageId) throws StorageException {
        ForumMessage message = messageCache.getObject(messageId);
        if (message == null) {
            message = storage.getMessageById(messageId);
            if (message != null) {
                messageCache.putObject(messageId, message);
            }
        }
        return message;
    }

    public int[] getMessageIdsByParentId(int parentMessageId) throws StorageException {
        return storage.getMessageIdsByParentId(parentMessageId);
    }

    public int[] getMessageIdsByTopicId(int topicId) throws StorageException {
        return storage.getMessageIdsByTopicId(topicId);
    }

    public int[] getMessageIdsByUserId(int userId) throws StorageException {
        return storage.getMessageIdsByUserId(userId);
    }

    public int[] getMessageIdsByForumId(int forumId) throws StorageException {
        return storage.getMessageIdsByForumId(forumId);
    }

    public int[] getMessageIdsByParentAndTopicIds(int parentId, int topicId) throws StorageException {
        return storage.getMessageIdsByParentAndTopicIds(parentId, topicId);
    }

    public int[] getAllMessageIds() throws StorageException {
        return storage.getAllMessageIds();
    }

    public ModerateInfo getModerateInfoById(int id) throws StorageException {
        ModerateInfo forum = moderateCache.getObject(id);
        if (forum == null) {
            forum = storage.getModerateInfoById(id);
            if (forum != null) {
                moderateCache.putObject(id, forum);
            }
        }
        return forum;
    }

    public int[] getModerateIdsByMessageId(int messageId) throws StorageException {
        return storage.getModerateIdsByMessageId(messageId);
    }

    public int[] getModerateIdsByUserIds(int userId) throws StorageException {
        return storage.getModerateIdsByUserIds(userId);
    }

    public int[] getModerateIdsByForumIds(int forumId) throws StorageException {
        return storage.getModerateIdsByForumIds(forumId);
    }

    public int[] getAllModerateIds() throws StorageException {
        return storage.getAllModerateIds();
    }

    public NewMessage getNewMessageById(int id) throws StorageException {
        return storage.getNewMessageById(id);
    }

    public int[] getAllNewMessageIds() throws StorageException {
        return storage.getAllNewMessageIds();
    }

    public NewRating getNewRating(int id) throws StorageException {
        return storage.getNewRating(id);
    }

    public int[] getAllNewRatingIds() throws StorageException {
        return storage.getAllNewRatingIds();
    }

    public Rating getRatingById(int id) throws StorageException {
        Rating rating = ratingCache.getObject(id);
        if (rating == null) {
            rating = storage.getRatingById(id);
            if (rating != null) {
                ratingCache.putObject(id, rating);
            }
        }
        return rating;
    }

    public int[] getRatingIdsByMessageId(int messageId) throws StorageException {
        return storage.getRatingIdsByMessageId(messageId);
    }

    public int[] getAllRatingIds() throws StorageException {
        return storage.getAllRatingIds();
    }

    public User getUserById(int id) throws StorageException {
        User user = userCache.getObject(id);
        if (user == null) {
            user = storage.getUserById(id);
            if (user != null) {
                userCache.putObject(id, user);
            }
        }
        return user;
    }

    public int[] getAllUserIds() throws StorageException {
        return storage.getAllUserIds();
    }
}
