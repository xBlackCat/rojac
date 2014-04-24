package org.xblackcat.rojac.service.storage.schema;

import org.xblackcat.sjpu.storage.IAH;
import org.xblackcat.sjpu.storage.StorageException;
import org.xblackcat.sjpu.storage.ann.DDL;

/**
 * 24.04.2014 11:15
 *
 * @author xBlackCat
 */
public interface IInitAH extends IAH {
    @DDL("CREATE TABLE forum_group (\n" +
                 "  id         INT PRIMARY KEY,\n" +
                 "  name       VARCHAR(64),\n" +
                 "  sort_order INT\n" +
                 ")")
    void createTableForumGroup() throws StorageException;

    @DDL("CREATE TABLE forum (\n" +
                 "  id             INT PRIMARY KEY,\n" +
                 "  forum_group_id INT,\n" +
                 "  rated          INT,\n" +
                 "  in_top         INT,\n" +
                 "  rate_limit     INT,\n" +
                 "  short_name     VARCHAR,\n" +
                 "  name           VARCHAR,\n" +
                 "  totalposts     INT DEFAULT 0,\n" +
                 "  lastpost_date  TIMESTAMP DEFAULT 0\n" +
                 ")")
    void createTableForum() throws StorageException;

    @DDL("CREATE TABLE new_rating (\n" +
                 "  id         INT NOT NULL AUTO_INCREMENT PRIMARY KEY,\n" +
                 "  user_id    INT,\n" +
                 "  message_id INT,\n" +
                 "  rate       INT\n" +
                 ")")
    void createTableNewRating() throws StorageException;

    @DDL("CREATE TABLE rating (\n" +
                 "  message_id  INT,\n" +
                 "  topic_id    INT,\n" +
                 "  user_id     INT,\n" +
                 "  user_rating INT,\n" +
                 "  rate        INT,\n" +
                 "  rate_date   TIMESTAMP,\n" +
                 "  PRIMARY KEY (message_id, user_id, rate)\n" +
                 ")")
    void createTableRating() throws StorageException;

    @DDL("CREATE TABLE version (\n" +
                 "  type    INT PRIMARY KEY,\n" +
                 "  version BINARY(64)\n" +
                 ")")
    void createTableVersion() throws StorageException;

    @DDL("CREATE TABLE moderate (\n" +
                 "  message_id    INT,\n" +
                 "  user_id       INT,\n" +
                 "  forum_id      INT,\n" +
                 "  creation_time TIMESTAMP,\n" +
                 "  PRIMARY KEY (message_id, user_id)\n" +
                 ")")
    void createTableModerate() throws StorageException;

    @DDL({
                 "CREATE TABLE new_message (\n" +
                         "  id          INT PRIMARY KEY,\n" +
                         "  user_own_id INT,\n" +
                         "  parent_id   INT,\n" +
                         "  forum_id    INT,\n" +
                         "  subject     VARCHAR,\n" +
                         "  message     VARCHAR,\n" +
                         "  draft       BOOLEAN\n" +
                         ")",
                 "CREATE INDEX idx_new_messages_to_sent ON new_message (user_own_id, draft)"
         })
    void createTableNewMessage() throws StorageException;

    @DDL({
                 "CREATE TABLE new_moderate (\n" +
                         "  id           INT PRIMARY KEY,\n" +
                         "  user_own_id  INT,\n" +
                         "  message_id   INT,\n" +
                         "  forum_id     INT,\n" +
                         "  action       INT,\n" +
                         "  as_moderator BOOLEAN,\n" +
                         "  description  VARCHAR\n" +
                         ")",
                 "CREATE INDEX idx_moderates_to_sent ON new_moderate (user_own_id)"
         })
    void createTableNewModerate() throws StorageException;

    @DDL("CREATE TABLE user (\n" +
                 "  id             INT PRIMARY KEY,\n" +
                 "  user_class     INT DEFAULT 0,\n" +
                 "  name           VARCHAR,\n" +
                 "  nick           VARCHAR,\n" +
                 "  real_name      VARCHAR,\n" +
                 "  email          VARCHAR,\n" +
                 "  home_page      VARCHAR,\n" +
                 "  specialization VARCHAR,\n" +
                 "  where_from     VARCHAR,\n" +
                 "  origin         VARCHAR\n" +
                 ")")
    void createTableUser() throws StorageException;

    @DDL("CREATE TABLE message (\n" +
                 "  id               INT PRIMARY KEY,\n" +
                 "  topic_id         INT,\n" +
                 "  parent_id        INT,\n" +
                 "  parent_user_id   INT,\n" +
                 "  user_id          INT,\n" +
                 "  forum_id         INT,\n" +
                 "  article_id       INT,\n" +
                 "  user_title_color INT,\n" +
                 "  user_role        INT,\n" +
                 "  category         INT NULL,\n" +
                 "  message_date     TIMESTAMP,\n" +
                 "  update_date      TIMESTAMP,\n" +
                 "  moderated_date   TIMESTAMP,\n" +
                 "  subject          VARCHAR,\n" +
                 "  message_name     VARCHAR,\n" +
                 "  user_nick        VARCHAR,\n" +
                 "  user_title       VARCHAR,\n" +
                 "  message          VARCHAR,\n" +
                 "  rating           VARCHAR\n" +
                 ")")
    void createTableMessage() throws StorageException;

    @DDL("CREATE TABLE extra_message(\n" +
                 "  message_id INT PRIMARY KEY\n" +
                 ")")
    void createTableExtraMessage() throws StorageException;

    @DDL({
                 "CREATE TABLE favorite (\n" +
                         "  id      INT PRIMARY KEY,\n" +
                         "  user_id INT,\n" +
                         "  type    VARCHAR,\n" +
                         "  config  VARCHAR\n" +
                         ")",
                 "CREATE INDEX idx_my_favorites ON favorite (user_id)"
         })
    void createTableFavorite() throws StorageException;

    @DDL({
                 "CREATE TABLE topic (\n" +
                         "  topic_id      INT PRIMARY KEY,\n" +
                         "  forum_id      INT NOT NULL,\n" +
                         "  own_posts     ARRAY,\n" +
                         "  post_count    INT,\n" +
                         "  lastpost_date TIMESTAMP,\n" +
                         "  topic_date    TIMESTAMP\n" +
                         ")",
                 "CREATE INDEX idx_forum_topics ON topic (forum_id, lastpost_date)"
         })
    void createTableTopic() throws StorageException;

    @DDL("CREATE TABLE topic_meta (\n" +
                 "  user_own_id   INT,\n" +
                 "  topic_id      INT,\n" +
                 "  forum_id      INT,\n" +
                 "  replies       INT,\n" +
                 "  unreadreplies INT,\n" +
                 "  ignored       BOOLEAN DEFAULT FALSE,\n" +
                 "  PRIMARY KEY (user_own_id, topic_id)\n" +
                 ")")
    void createTableTopicMeta() throws StorageException;

    @DDL("CREATE TABLE message_meta (\n" +
                 "  user_own_id      INT,\n" +
                 "  message_id       INT,\n" +
                 "  topic_id         INT,\n" +
                 "  forum_id         INT,\n" +
                 "  read             BOOLEAN,\n" +
                 "  ignored          BOOLEAN,\n" +
                 "  response         BOOLEAN,\n" +
                 "  notifyonresponse BOOLEAN,\n" +
                 "  PRIMARY KEY (user_own_id, message_id)\n" +
                 ")")
    void createTableMessageMeta() throws StorageException;

    @DDL("CREATE TABLE forum_meta (\n" +
                 "  user_own_id INT,\n" +
                 "  forum_id    INT,\n" +
                 "  subscribed  BOOLEAN,\n" +
                 "  PRIMARY KEY (user_own_id, forum_id)\n" +
                 ")")
    void createTableForumMeta() throws StorageException;

    @DDL("CREATE TABLE user_meta (\n" +
                 "  user_own_id INT,\n" +
                 "  user_id    INT,\n" +
                 "  ignored  BOOLEAN,\n" +
                 "  PRIMARY KEY (user_own_id, user_id)\n" +
                 ")")
    void createTableUserMeta() throws StorageException;
}
