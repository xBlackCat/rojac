package org.xblackcat.rojac.service.storage;

/**
 * @author ASUS
 */

public interface IMiscAH extends AH {
    void storeExtraMessage(int messageId) throws StorageException;

    void removeExtraMessage(int messageId) throws StorageException;

    void addToIgnoredTopicList(int topicId) throws StorageException;

    void removeFromIgnoredTopicList(int topicId) throws StorageException;

    void addToIgnoredUserList(int userId) throws StorageException;

    void removeFromIgnoredUserList(int userId) throws StorageException;

    int getAmountOfIgnoredTopics() throws StorageException;

    void clearExtraMessages() throws StorageException;

    int[] getExtraMessages() throws StorageException;
}
