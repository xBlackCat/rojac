package org.xblackcat.rojac.service.janus.data;

import org.xblackcat.rojac.data.Version;
import ru.rsdn.janus.JanusMessageInfo;
import ru.rsdn.janus.JanusModerateInfo;
import ru.rsdn.janus.JanusRatingInfo;

import java.util.List;

/**
 * @author ASUS
 */

public class NewData extends TopicMessages {
    private final int ownUserId;

    private final Version forumRowVersion;
    private final Version ratingRowVersion;
    private final Version moderateRowVersion;

    public NewData(int ownUserId, Version forumRowVersion, Version ratingRowVersion, Version moderateRowVersion, List<JanusMessageInfo> mes, List<JanusModerateInfo> mod, List<JanusRatingInfo> r) {
        super(mes, mod, r);
        this.ownUserId = ownUserId;
        this.forumRowVersion = forumRowVersion;
        this.ratingRowVersion = ratingRowVersion;
        this.moderateRowVersion = moderateRowVersion;
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
}
