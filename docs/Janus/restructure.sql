CREATE TABLE [download_topics] ( [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	 [source] NVARCHAR(32),
	 [messageid] INTEGER NOT NULL,
	 [hint] NVARCHAR(128))
CREATE TABLE [favorites] ( [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	 [mid] INTEGER NOT NULL,
	 [fid] INTEGER NOT NULL,
	 [comment] NVARCHAR(255),
	 [url] NTEXT)
CREATE TABLE [favorites_folders] ( [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	 [name] NVARCHAR(100),
	 [pid] INTEGER NOT NULL,
	 [comment] NVARCHAR(255))
CREATE TABLE [marks_outbox] ( [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	 [mark] INTEGER NOT NULL,
	 [mid] INTEGER NOT NULL)
CREATE TABLE [messages] ( [dte] TIMESTAMP NOT NULL,
	 [gid] INTEGER NOT NULL,
	 [ismarked] BIT DEFAULT 0 NOT NULL,
	 [isread] TINYINT DEFAULT 0 NOT NULL,
	 [message] NTEXT,
	 [mid] INTEGER NOT NULL,
	 [pid] INTEGER NOT NULL,
	 [subject] NVARCHAR(128),
	 [tid] INTEGER NOT NULL,
	 [uclass] INTEGER,
	 [uid] INTEGER NOT NULL,
	 [usernick] NVARCHAR(50),
	 [article_id] INTEGER,
	 [readreplies] BIT DEFAULT 0 NOT NULL,
	 [name] NVARCHAR(160),
	 [lastModerated] TIMESTAMP,
	 [closed] BIT DEFAULT 0 NOT NULL,
	PRIMARY KEY ([mid]))
CREATE TABLE [messages_outbox] ( [dte] TIMESTAMP NOT NULL,
	 [gid] INTEGER,
	 [hold] BIT DEFAULT 0 NOT NULL,
	 [message] NTEXT,
	 [mid] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	 [reply] INTEGER NOT NULL,
	 [subject] NVARCHAR(128),
	 [tagline] NVARCHAR(128))
CREATE TABLE [rate_outbox] ( [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	 [mid] INTEGER NOT NULL,
	 [rate] INTEGER NOT NULL)
CREATE TABLE [rating] ( [dte] TIMESTAMP NOT NULL,
	 [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	 [mid] INTEGER NOT NULL,
	 [rate] SMALLINT NOT NULL,
	 [rby] SMALLINT NOT NULL,
	 [tid] INTEGER NOT NULL,
	 [uid] INTEGER NOT NULL)
CREATE TABLE [server_forums] ( [descript] NVARCHAR(128),
	 [id] INTEGER NOT NULL,
	 [name] NVARCHAR(64),
	 [rated] BIT DEFAULT 0 NOT NULL,
	 [intop] BIT DEFAULT 0 NOT NULL,
	 [ratelimit] SMALLINT NOT NULL,
	PRIMARY KEY ([id]))
CREATE TABLE [subscribed_forums] ( [descript] NVARCHAR(128),
	 [id] INTEGER NOT NULL,
	 [lastsync] INTEGER NOT NULL,
	 [name] NVARCHAR(64),
	 [urcount] INTEGER,
	 [issync] BIT DEFAULT 0 NOT NULL,
	 [priority] INTEGER,
	PRIMARY KEY ([id]))
CREATE TABLE [topic_info] ( [mid] INTEGER NOT NULL,
	 [answers_count] SMALLINT,
	 [answers_unread] SMALLINT,
	 [answers_rate] SMALLINT,
	 [answers_smile] SMALLINT,
	 [answers_agree] SMALLINT,
	 [answers_disagree] SMALLINT,
	 [answers_me_unread] SMALLINT,
	 [answers_marked] SMALLINT,
	 [answers_last_update_date] TIMESTAMP,
	 [answers_mod_count] SMALLINT,
	 [this_rate] SMALLINT,
	 [this_smile] SMALLINT,
	 [this_agree] SMALLINT,
	 [this_disagree] SMALLINT,
	 [this_mod_count] SMALLINT,
	 [gid] INTEGER,
	PRIMARY KEY ([mid]),
	FOREIGN KEY ([mid]) REFERENCES [messages] ([mid]))
CREATE TABLE [users] ( [uid] INTEGER NOT NULL,
	 [homepage] NVARCHAR(120),
	 [origin] NVARCHAR(255),
	 [publicemail] NVARCHAR(60),
	 [realname] NVARCHAR(80),
	 [spec] NVARCHAR(100),
	 [userclass] INTEGER,
	 [username] NVARCHAR(60),
	 [usernick] NVARCHAR(100),
	 [wherefrom] NVARCHAR(100),
	PRIMARY KEY ([uid]))
CREATE TABLE [vars] ( [name] NVARCHAR(24) NOT NULL,
	 [varvalue] NVARCHAR(128),
	PRIMARY KEY ([name]))
CREATE TABLE [moderatorials] ( [messageId] INTEGER NOT NULL,
	 [userId] INTEGER NOT NULL,
	 [forumId] INTEGER NOT NULL,
	 [create] TIMESTAMP NOT NULL,
	PRIMARY KEY ([messageId],[userId]))
CREATE   INDEX [IX_messages_dte] ON [messages] ([dte])
CREATE   INDEX [IX_messages_gid] ON [messages] ([gid])
CREATE   INDEX [IX_messages_pid] ON [messages] ([pid])
CREATE   INDEX [IX_messages_tid] ON [messages] ([tid])
CREATE   INDEX [IX_messages_uid] ON [messages] ([uid])
CREATE   INDEX [IX_rating_mid] ON [rating] ([mid])
CREATE   INDEX [IX_rating_mid_rate] ON [rating] ([mid], [rate])
CREATE   INDEX [IX_rating_tid] ON [rating] ([tid])
CREATE   INDEX [IX_rating_uid] ON [rating] ([uid])
CREATE   INDEX [IX_server_forums_name] ON [server_forums] ([name])
CREATE   INDEX [IX_subscribed_forums_name] ON [subscribed_forums] ([name])
CREATE   INDEX [IX_ti_answers_last] ON [topic_info] ([answers_last_update_date])
CREATE   INDEX [IX_topic_info_gid] ON [topic_info] ([gid])
CREATE   INDEX [IX_moderatorials_messageId] ON [moderatorials] ([messageId])
CREATE   INDEX [IX_moderatorials_userId] ON [moderatorials] ([userId])
CREATE   INDEX [IX_moderatorials_create] ON [moderatorials] ([create])
