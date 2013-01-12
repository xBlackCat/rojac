package org.xblackcat.rojac.data;

import ru.rsdn.janus.PostModerateInfo;

/**
 * @author xBlackCat
 */

public class NewModerate implements IRSDNable<PostModerateInfo> {
    private final int id;
    private final int messageId;
    private final ModerateAction action;
    private final int forumId;
    private final String description;
    private final boolean asModerator;

    public NewModerate(int id, int messageId, int action, int forumId, String description, boolean asModerator) {
        this.id = id;
        this.messageId = messageId;
        this.action = ModerateAction.getAction(action);
        this.forumId = forumId;
        this.description = description;
        this.asModerator = asModerator;
    }

    public int getId() {
        return id;
    }

    public int getMessageId() {
        return messageId;
    }

    public ModerateAction getAction() {
        return action;
    }

    public int getForumId() {
        return forumId;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAsModerator() {
        return asModerator;
    }

    public PostModerateInfo getRSDNObject() {
        return new PostModerateInfo(
/*
                id,
                messageId,
                action.getType(),
                forumId,
                description,
                asModerator
*/
        );
    }
}
