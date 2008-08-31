package org.xblackcat.rojac.data;

import ru.rsdn.Janus.JanusForumGroupInfo;

/**
 * Date: 12 квіт 2007
 *
 * @author Alexey
 */

public final class ForumGroup {
    private final int forumGroupId;
    private final String forumGroupName;
    private final int sortOrder;

    public ForumGroup(int forumGroupId, String forumGroupName, int sortOrder) {
        this.forumGroupId = forumGroupId;
        this.forumGroupName = forumGroupName;
        this.sortOrder = sortOrder;
    }

    public ForumGroup(JanusForumGroupInfo i) {
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

        ForumGroup that = (ForumGroup) o;

        if (forumGroupId != that.forumGroupId) return false;

        return true;
    }

    public int hashCode() {
        return forumGroupId;
    }

    public String toString() {
        StringBuilder str = new StringBuilder("ForumGroup[");
        str.append("forumGroupId=").append(forumGroupId).append(", ");
        str.append("forumGroupName=").append(forumGroupName).append(", ");
        str.append("sortOrder=").append(sortOrder).append(']');
        return str.toString();
    }

}
