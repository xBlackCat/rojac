package org.xblackcat.sunaj.service.janus.data;

import org.xblackcat.sunaj.service.data.Forum;
import org.xblackcat.sunaj.service.data.ForumGroup;
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
    private final Forum[] forums;
    private final ForumGroup[] fourumGroups;

    public ForumsList(JanusForumInfo[] infos, JanusForumGroupInfo[] groups) {
        forums = new Forum[infos.length];
        for (int i = 0; i < infos.length; i++) {
            forums[i] = new Forum(infos[i]);
        }

        fourumGroups = new ForumGroup[groups.length];
        for (int i = 0; i < groups.length; i++) {
            fourumGroups[i] = new ForumGroup(groups[i]);
        }
    }

    public Forum[] getForums() {
        return forums;
    }

    public ForumGroup[] getFourumGroups() {
        return fourumGroups;
    }
}
