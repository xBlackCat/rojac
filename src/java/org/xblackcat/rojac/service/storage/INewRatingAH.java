package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.Mark;
import org.xblackcat.rojac.data.NewRating;
import org.xblackcat.sjpu.storage.IAH;
import org.xblackcat.sjpu.storage.StorageException;
import org.xblackcat.sjpu.storage.ann.MapRowTo;
import org.xblackcat.sjpu.storage.ann.Sql;

import java.util.List;

/**
 * @author ASUS
 */

public interface INewRatingAH extends IAH {
    @Sql("INSERT INTO new_rating(message_id, rate) VALUES (?, ?)")
    void storeNewRating(int messageId, Mark rate) throws StorageException;

    @Sql("DELETE FROM new_rating WHERE id=?")
    boolean removeNewRating(int id) throws StorageException;

    @Sql("SELECT id, message_id, rate FROM new_rating WHERE message_id=?")
    @MapRowTo(NewRating.class)
    List<NewRating> getNewRatingsByMessageId(int messageId) throws StorageException;

    @Sql("SELECT id, message_id, rate FROM new_rating")
    @MapRowTo(NewRating.class)
    List<NewRating> getAllNewRatings() throws StorageException;

    @Sql("DELETE FROM new_rating")
    void clearRatings() throws StorageException;

    @Sql("SELECT rate FROM new_rating WHERE message_id=?")
    @MapRowTo(Mark.class)
    List<Mark> getNewRatingMarksByMessageId(int messageId) throws StorageException;
}
