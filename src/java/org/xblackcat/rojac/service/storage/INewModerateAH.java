package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.ModerateAction;
import org.xblackcat.rojac.data.NewModerate;
import org.xblackcat.sjpu.storage.IAH;
import org.xblackcat.sjpu.storage.StorageException;
import org.xblackcat.sjpu.storage.ann.MapRowTo;
import org.xblackcat.sjpu.storage.ann.Sql;

import java.util.List;

/**
 * @author ASUS
 */

public interface INewModerateAH extends IAH {
    @Sql("INSERT INTO new_moderate(id, message_id, action, forum_id, description, as_moderator) VALUES (?, ?, ?, ?, ?, ?)")
    void storeNewModerate(
            int id,
            int messageId,
            ModerateAction action,
            int forumId,
            String description,
            boolean asModerator
    ) throws StorageException;

    @Sql("DELETE FROM new_moderate WHERE id=?")
    int removeNewModerate(int id) throws StorageException;

    @Sql("SELECT id, message_id, action, forum_id, description, as_moderator FROM new_moderate WHERE id=?")
    NewModerate getNewModerateById(int id) throws StorageException;

    @Sql("SELECT id, message_id, action, forum_id, description, as_moderator FROM new_moderate")
    @MapRowTo(NewModerate.class)
    List<NewModerate> getAllNewModerates() throws StorageException;
}