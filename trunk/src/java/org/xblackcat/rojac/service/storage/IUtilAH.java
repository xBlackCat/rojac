package org.xblackcat.rojac.service.storage;

import org.xblackcat.sjpu.storage.IAH;
import org.xblackcat.sjpu.storage.StorageException;
import org.xblackcat.sjpu.storage.ann.MapRowTo;
import org.xblackcat.sjpu.storage.ann.Sql;

import java.util.List;

/**
 * 04.04.12 17:10
 *
 * @author xBlackCat
 */
public interface IUtilAH extends IAH {
    @Sql("UPDATE forum f SET f.totalposts = ((SELECT count(mm.id) FROM message mm WHERE mm.forum_id = f.id)), f.lastpost_date = ((SELECT max(mm.message_date) FROM message mm WHERE mm.forum_id = f.id))")
    void updateForumsStat() throws StorageException;

    @Sql("UPDATE message m SET m.replies = ((SELECT count(mm.id) FROM message mm WHERE mm.topic_id = m.id AND mm.forum_id = m.forum_id)) WHERE m.topic_id = 0")
    void updateThreadsStat() throws StorageException;

    @Sql("MERGE INTO topic_cache (topic_id, forum_id, ignored, lastpost_id, replies) SELECT m.id,m.forum_id,IFNULL((SELECT tco.ignored FROM topic_cache tco WHERE tco.topic_id = m.topic_id), FALSE) AS ignored, ((SELECT max(mm.id) FROM message mm WHERE mm.topic_id = m.id AND mm.forum_id = m.forum_id)) AS lastpost_id,((SELECT count(mr.id) FROM message mr WHERE mr.topic_id = m.id AND mr.forum_id = m.forum_id)) AS replies FROM message m WHERE m.topic_id = 0")
    void updateLastPostId() throws StorageException;

    @Sql("UPDATE topic_cache tc SET tc.lastpost_date = IFNULL((SELECT mm.message_date FROM message mm WHERE mm.id = tc.lastpost_id), (SELECT m.message_date FROM message m WHERE m.id = tc.topic_id))")
    void updateLastPostDate() throws StorageException;

    @Sql("UPDATE message m SET m.parent_user_id=IFNULL((SELECT pm.user_id FROM message pm WHERE pm.id = m.parent_id AND m.forum_id = pm.forum_id), 0)")
    void updateParentUserId() throws StorageException;

    @Sql("DELETE FROM message WHERE id = ?")
    void removeTopic(int topicId) throws StorageException;

    @Sql("SELECT count(mr.id) FROM message mr, message m WHERE (mr.topic_id = m.id OR mr.id = m.id) AND mr.forum_id = m.forum_id AND m.lastpost_date < ? AND (? OR m.read) AND (? OR (SELECT tc.ignored FROM topic_cache tc WHERE tc.topic_id = CASEWHEN(m.topic_id = 0, m.id, m.topic_id)) = FALSE)")
    int getTopicsAmountToClean(long timeLine, boolean onlyRead, boolean onlyIgnored) throws StorageException;

    @Sql("SELECT mr.id FROM message mr, message m WHERE (mr.topic_id = m.id OR mr.id = m.id) AND mr.forum_id = m.forum_id AND m.lastpost_date < ? AND (? OR m.read) AND (? OR (SELECT tc.ignored FROM topic_cache tc WHERE tc.topic_id = CASEWHEN(m.topic_id = 0, m.id, m.topic_id)) = FALSE)")
    @MapRowTo(Integer.class)
    List<Integer> getTopicIdsToClean(long timeLine, boolean onlyRead, boolean onlyIgnored) throws StorageException;
}
