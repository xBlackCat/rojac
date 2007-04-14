package org.xblackcat.sunaj.service.janus.data;

import ru.rsdn.Janus.JanusForumInfo;

/**
 * Date: 12 квіт 2007
 *
 * @author Alexey
 */

public final class ForumInfo {
    private final int forumId;
    private final int forumGroupId;
    private final String shortForumName;
    private final String forumName;
    private final int rated;
    private final int inTop;
    private final int rateLimit;

    public ForumInfo(int forumId, int forumGroupId, int inTop, int rated, int rateLimit, String shortForumName, String forumName) {
        this.forumGroupId = forumGroupId;
        this.forumId = forumId;
        this.forumName = forumName;
        this.inTop = inTop;
        this.rated = rated;
        this.rateLimit = rateLimit;
        this.shortForumName = shortForumName;
    }

    public ForumInfo(JanusForumInfo i) {
        this(i.getForumId(), i.getForumGroupId(), i.getInTop(), i.getRated(), i.getRateLimit(), i.getShortForumName(), i.getForumName());
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

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ForumInfo forumInfo = (ForumInfo) o;

        if (forumGroupId != forumInfo.forumGroupId) return false;
        if (forumId != forumInfo.forumId) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = forumId;
        result = 31 * result + forumGroupId;
        return result;
    }
}
