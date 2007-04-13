package org.xblackcat.sunaj.service.soap.data;

import ru.rsdn.Janus.JanusForumGroupInfo;
import ru.rsdn.Janus.JanusForumInfo;

/**
 * Class for holding forums list.
 * <p/>
 * Date: 12 квіт 2007
 *
 * @author Alexey
 */

public final class ForumsList {
    private final ForumInfo[] forums;
    private final ForumGroupInfo[] fourumGroups;

    public ForumsList(JanusForumInfo[] infos, JanusForumGroupInfo[] groups) {
        forums = new ForumInfo[infos.length];
        for (int i = 0; i < infos.length; i++) {
            forums[i] = new ForumInfo(infos[i]);
        }

        fourumGroups = new ForumGroupInfo[groups.length];
        for (int i = 0; i < groups.length; i++) {
            fourumGroups[i] = new ForumGroupInfo(groups[i]);
        }
    }

    public ForumInfo[] getForums() {
        return forums;
    }

    public ForumGroupInfo[] getFourumGroups() {
        return fourumGroups;
    }
}
