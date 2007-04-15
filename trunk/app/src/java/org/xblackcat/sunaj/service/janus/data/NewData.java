package org.xblackcat.sunaj.service.janus.data;

import org.xblackcat.sunaj.service.data.ForumMessage;
import org.xblackcat.sunaj.service.data.ModerateInfo;
import org.xblackcat.sunaj.service.data.Rating;
import org.xblackcat.sunaj.service.data.Version;
import ru.rsdn.Janus.JanusMessageInfo;
import ru.rsdn.Janus.JanusModerateInfo;
import ru.rsdn.Janus.JanusRatingInfo;

/**
 * Date: 15.04.2007
 *
 * @author ASUS
 */

public final class NewData {
    private final int ownUserId;

    private final Version forumRowVersion;
    private final Version ratingRowVersion;
    private final Version moderateRowVersion;

    private final ForumMessage[] messages;
    private final Rating[] ratings;
    private final ModerateInfo[] moderates;

    public NewData(int ownUserId, Version forumRowVersion, Version ratingRowVersion, Version moderateRowVersion, JanusMessageInfo[] mes, JanusModerateInfo[] mod, JanusRatingInfo[] r) {
        this.ownUserId = ownUserId;
        this.forumRowVersion = forumRowVersion;
        this.ratingRowVersion = ratingRowVersion;
        this.moderateRowVersion = moderateRowVersion;

        messages = new ForumMessage[mes.length];
        for (int i = 0; i < mes.length; i++) {
            messages[i] = new ForumMessage(mes[i]);
        }

        moderates = new ModerateInfo[mod.length];
        for (int i = 0; i < mod.length; i++) {
            moderates[i] = new ModerateInfo(mod[i]);
        }

        ratings = new Rating[r.length];
        for (int i = 0; i < r.length; i++) {
            ratings[i] = new Rating(r[i]);
        }
    }

    public int getOwnUserId() {
        return ownUserId;
    }

    public Version getForumRowVersion() {
        return forumRowVersion;
    }

    public Version getRatingRowVersion() {
        return ratingRowVersion;
    }

    public Version getModerateRowVersion() {
        return moderateRowVersion;
    }

    public ForumMessage[] getMessages() {
        return messages;
    }

    public Rating[] getRatings() {
        return ratings;
    }

    public ModerateInfo[] getModerates() {
        return moderates;
    }
}