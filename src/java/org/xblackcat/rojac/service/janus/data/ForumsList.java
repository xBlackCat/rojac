package org.xblackcat.rojac.service.janus.data;

import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ForumGroup;
import org.xblackcat.rojac.data.Version;
import ru.rsdn.janus.JanusForumGroupInfo;
import ru.rsdn.janus.JanusForumInfo;

import java.util.List;

/**
 * Class for holding forums list.
 * <p/>
 *
 * @author Alexey
 */

public class ForumsList {
    private final Forum[] forums;
    private final ForumGroup[] fourumGroups;
    private final Version version;

    public ForumsList(List<JanusForumInfo> infos, List<JanusForumGroupInfo> groups, Version version) {
        this.version = version;
        forums = new Forum[infos.size()];
        int i = 0;
        for (JanusForumInfo jfi : infos) {
            forums[i++] = new Forum(jfi);
        }

        fourumGroups = new ForumGroup[groups.size()];
        i=0;
        for (JanusForumGroupInfo fgi : groups) {
            fourumGroups[i++] = new ForumGroup(fgi);
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
