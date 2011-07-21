package org.xblackcat.rojac.gui.view.forumlist;

import org.xblackcat.rojac.gui.IViewLayout;

import java.util.Set;

/**
* @author xBlackCat Date: 21.07.11
*/
class ForumListViewLayout implements IViewLayout {
    private final Set<ForumFilterState> state;

    public ForumListViewLayout(Set<ForumFilterState> state) {
        this.state = state;
    }

    public Set<ForumFilterState> getStates() {
        return state;
    }
}
