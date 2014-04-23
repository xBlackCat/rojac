package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.Mark;
import org.xblackcat.rojac.data.MarkStat;
import org.xblackcat.rojac.data.Rating;
import org.xblackcat.sjpu.storage.IAH;
import org.xblackcat.sjpu.storage.StorageException;
import org.xblackcat.sjpu.storage.ann.MapRowTo;
import org.xblackcat.sjpu.storage.ann.Sql;

import java.util.List;

/**
 * @author ASUS
 */

public interface IRatingAH extends IAH {
    @Sql("MERGE INTO rating(message_id, topic_id, user_id, user_rating, rate, rate_date) VALUES (?, ?, ?, ?, ?, ?)")
    void storeRating(int messageId, int topicId, int userId, int userRating, Mark mark, long timeInMillis) throws StorageException;

    @Sql("SELECT message_id, topic_id, user_id, user_rating, rate, rate_date FROM rating WHERE message_id=?")
    @MapRowTo(Rating.class)
    List<Rating> getRatingsByMessageId(int messageId) throws StorageException;

    @Sql("SELECT rate, count(user_id) FROM rating WHERE message_id = ? GROUP BY rate")
    @MapRowTo(MarkStat.class)
    List<MarkStat> getMarkStatByMessageId(int messageId) throws StorageException;
}
