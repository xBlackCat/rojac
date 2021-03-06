package org.xblackcat.rojac.data;

import org.xblackcat.sjpu.storage.ann.DefaultRowMap;
import ru.rsdn.janus.JanusForumInfo;

/**
 * @author Alexey
 */

public final class Forum {
    private final int forumId;
    private final int forumGroupId;
    private final String shortForumName;
    private final String forumName;
    private final int rated;
    private final int inTop;
    private final int rateLimit;
    private final boolean subscribed;

    @DefaultRowMap
    public Forum(
            int forumId,
            int forumGroupId,
            int rated, int inTop,
            int rateLimit,
            boolean subscribed,
            String shortForumName,
            String forumName
    ) {
        this.subscribed = subscribed;
        this.forumGroupId = forumGroupId;
        this.forumId = forumId;
        this.forumName = forumName;
        this.inTop = inTop;
        this.rated = rated;
        this.rateLimit = rateLimit;
        this.shortForumName = shortForumName;
    }

    public Forum(JanusForumInfo i) {
        this(i.getForumId(), i.getForumGroupId(), i.getRated(), i.getInTop(), i.getRateLimit(),
             false,
             i.getShortForumName(), i.getForumName()
        );
    }

    public int getForumGroupId() {
        return forumGroupId;
    }

    public int getForumId() {
        return forumId;
    }

    public String getForumName() {
        return forumName;
    }

    public int getInTop() {
        return inTop;
    }

    public int getRated() {
        return rated;
    }

    public int getRateLimit() {
        return rateLimit;
    }

    public String getShortForumName() {
        return shortForumName;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public Forum setSubscribed(boolean newSubscribed) {
        if (newSubscribed == subscribed) {
            return this;
        } else {
            return new Forum(
                    forumId,
                    forumGroupId,
                    rated, inTop,
                    rateLimit,
                    newSubscribed, shortForumName,
                    forumName
            );
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Forum forum = (Forum) o;

        return forumGroupId == forum.forumGroupId && forumId == forum.forumId;

    }

    public int hashCode() {
        int result;
        result = forumId;
        result = 31 * result + forumGroupId;
        return result;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(forumName).append("[");
        str.append(forumId).append(": ");
        str.append(shortForumName).append("]");
        return str.toString();
    }
}
