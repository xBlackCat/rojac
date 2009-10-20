package org.xblackcat.rojac.service.janus.data;

import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ForumGroup;
import org.xblackcat.rojac.data.Version;
import ru.rsdn.Janus.JanusForumGroupInfo;
import ru.rsdn.Janus.JanusForumInfo;

/**
 * Class for holding forums list.
 * <p/>
 *
 * @author Alexey
 */

public final class ForumsList {
    private final Forum[] forums;
    private final ForumGroup[] fourumGroups;
    private final Version version;

    public ForumsList(JanusForumInfo[] infos, JanusForumGroupInfo[] groups, Version version) {
        this.version = version;
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

    public ForumGroup[] getForumGroups() {
        return fourumGroups;
    }

    public Version getVersion() {
        return version;
    }
}
