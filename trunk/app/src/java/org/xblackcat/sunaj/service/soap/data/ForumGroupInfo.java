package org.xblackcat.sunaj.service.soap.data;

import ru.rsdn.Janus.JanusForumGroupInfo;

/**
 * Date: 12 квіт 2007
 *
 * @author Alexey
 */

public final class ForumGroupInfo {
    private final int forumGroupId;
    private final String forumGroupName;
    private final int sortOrder;

    public ForumGroupInfo(int forumGroupId, String forumGroupName, int sortOrder) {
        this.forumGroupId = forumGroupId;
        this.forumGroupName = forumGroupName;
        this.sortOrder = sortOrder;
    }

    public ForumGroupInfo(JanusForumGroupInfo i) {
        this(i.getForumGroupId(), i.getForumGroupName(), i.getSortOrder());
    }

    public int getForumGroupId() {
        return forumGroupId;
    }

    public String getForumGroupName() {
        return forumGroupName;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ForumGroupInfo that = (ForumGroupInfo) o;

        if (forumGroupId != that.forumGroupId) return false;

        return true;
    }

    public int hashCode() {
        return forumGroupId;
    }
}
