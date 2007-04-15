package org.xblackcat.sunaj.service.storage;

import org.xblackcat.sunaj.service.data.*;

/**
 * Date: 15.04.2007
 *
 * @author ASUS
 */

public interface IStorage {
    /* Initialization routines */

    boolean checkStructure() throws StorageException;

    void initialize() throws StorageException;

    /* Storing routines */

    void storeForum(Forum f) throws StorageException;

    void storeForumGroup(ForumGroup fg) throws StorageException;

    void storeForumMessage(ForumMessage fm) throws StorageException;

    void storeModerateInfo(ModerateInfo mi) throws StorageException;

    void storeNewMessage(NewMessage nm) throws StorageException;

    void storeNewRating(NewRating nr) throws StorageException;

    void storeRating(Rating ri) throws StorageException;

    void storeUser(User ui) throws StorageException;

    /* Removing routines */

    boolean removeForum(int id) throws StorageException;

    boolean removeForumGroup(int id) throws StorageException;

    boolean removeForumMessage(int id) throws StorageException;

    boolean removeModerateInfo(int id) throws StorageException;

    boolean removeNewMessage(int id) throws StorageException;

    boolean removeNewRating(int id) throws StorageException;

    boolean removeRating(int id) throws StorageException;

    boolean removeUser(int id) throws StorageException;

    /* Data extracting routines */

    Forum getForumById(int forumId) throws StorageException;

    int[] getForumIdsInGroup(int forumGroupId) throws StorageException;

    int[] getAllForumIds() throws StorageException;


    ForumGroup getForumGroupById(int forumGroupId) throws StorageException;

    int[] getAllForumGroupIds() throws StorageException;


    ForumMessage getMessageById(int messageId) throws StorageException;

    int[] getMessageIdsByParentId(int parentMessageId) throws StorageException;

    int[] getMessageIdsByTopicId(int topicId) throws StorageException;

    int[] getMessageIdsByUserId(int userId) throws StorageException;

    int[] getMessageIdsByForumId(int forumId) throws StorageException;

    int[] getMessageIdsByParentAndTopicIds(int parentId, int topicId) throws StorageException;

    int[] getAllMessageIds() throws StorageException;


    ModerateInfo getModerateInfoById(int id) throws StorageException;

    int[] getModerateIdsByMessageId(int messageId) throws StorageException;

    int[] getModerateIdsByUserIds(int userId) throws StorageException;

    int[] getModerateIdsByForumIds(int forumId) throws StorageException;

    int[] getAllModerateIds() throws StorageException;


    NewMessage getNewMessageById(int id) throws StorageException;

    int[] getAllNewMessageIds() throws StorageException;


    NewRating getNewRating(int id) throws StorageException;

    int[] getAllNewRatingIds() throws StorageException;


    Rating getRatingById(int id) throws StorageException;

    int[] getRatingIdsByMessageId(int messageId) throws StorageException;

    int[] getAllRatingIds() throws StorageException;


    User getUserById(int id) throws StorageException;

    int[] getAllUserIds() throws StorageException;
}
