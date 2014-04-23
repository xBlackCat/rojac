package org.xblackcat.rojac.service.storage;

import org.xblackcat.sjpu.storage.IAH;
import org.xblackcat.sjpu.storage.StorageException;
import org.xblackcat.sjpu.storage.ann.Sql;

/**
 * @author ASUS
 */

public interface IMiscAH extends IAH {
    @Sql("INSERT INTO extra_message(message_id) VALUES (?)")
    void storeExtraMessage(int messageId) throws StorageException;

    @Sql("DELETE FROM extra_message WHERE message_id=?")
    void removeExtraMessage(int messageId) throws StorageException;

    @Sql("UPDATE topic_cache SET ignored = TRUE WHERE topic_id = ?")
    void addToIgnoredTopicList(int topicId) throws StorageException;

    @Sql("UPDATE topic_cache SET ignored = FALSE WHERE topic_id = ?")
    void removeFromIgnoredTopicList(int topicId) throws StorageException;

    @Sql("UPDATE user SET ignored = TRUE WHERE id = ?")
    void addToIgnoredUserList(int userId) throws StorageException;

    @Sql("UPDATE user SET ignored = FALSE WHERE id = ?")
    void removeFromIgnoredUserList(int userId) throws StorageException;

    @Sql("SELECT count(*) FROM topic_cache WHERE ignored")
    int getAmountOfIgnoredTopics() throws StorageException;

    @Sql("DELETE FROM extra_message")
    void clearExtraMessages() throws StorageException;

    @Sql("SELECT message_id FROM extra_message")
    int[] getExtraMessages() throws StorageException;
}
