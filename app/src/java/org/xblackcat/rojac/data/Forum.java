package org.xblackcat.rojac.data;

import ru.rsdn.Janus.JanusForumInfo;

/**
 * Date: 12 квіт 2007
 *
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

    public Forum(int forumId, int forumGroupId, int inTop, int rated, int rateLimit, String shortForumName, String forumName, boolean subscribed) {
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
        this(i.getForumId(), i.getForumGroupId(), i.getInTop(), i.getRated(), i.getRateLimit(), i.getShortForumName(), i.getForumName(), false);
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

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Forum forum = (Forum) o;

        if (forumGroupId != forum.forumGroupId) return false;
        if (forumId != forum.forumId) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = forumId;
        result = 31 * result + forumGroupId;
        return result;
    }

    public String toString() {
        StringBuilder str = new StringBuilder("Forum[");
        str.append("forumId=").append(forumId).append(", ");
        str.append("forumGroupId=").append(forumGroupId).append(", ");
        str.append("shortForumName=").append(shortForumName).append(", ");
        str.append("forumName=").append(forumName).append(", ");
        str.append("rated=").append(rated).append(", ");
        str.append("inTop=").append(inTop).append(", ");
        str.append("rateLimit=").append(rateLimit).append(']');
        return str.toString();
    }
}
