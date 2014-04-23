package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.Moderate;
import org.xblackcat.sjpu.storage.IAH;
import org.xblackcat.sjpu.storage.StorageException;
import org.xblackcat.sjpu.storage.ann.MapRowTo;
import org.xblackcat.sjpu.storage.ann.RowMap;
import org.xblackcat.sjpu.storage.ann.Sql;

import java.util.List;

/**
 * @author ASUS
 */

public interface IModerateAH extends IAH {
    @Sql("MERGE INTO moderate(message_id, user_id, forum_id, creation_time) VALUES (?, ?, ?, ?)")
    void storeModerateInfo(int messageId, int userId, int forumId, long createDate) throws StorageException;

    @Sql("DELETE FROM moderate WHERE message_id=?")
    boolean removeModerateInfosByMessageId(int id) throws StorageException;

    @Sql("SELECT message_id, user_id, forum_id, creation_time FROM moderate WHERE message_id=?")
    @MapRowTo(Moderate.class)
    @RowMap({int.class, int.class, int.class, long.class})
    List<Moderate> getModeratesByMessageId(int messageId) throws StorageException;

}
