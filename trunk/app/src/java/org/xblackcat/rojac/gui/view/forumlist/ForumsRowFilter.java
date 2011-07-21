package org.xblackcat.rojac.gui.view.forumlist;

import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ForumStatistic;

import javax.swing.*;
import java.util.EnumSet;

/**
 * @author xBlackCat
 */
class ForumsRowFilter extends RowFilter<ForumTableModel, Integer> {
    private final EnumSet<ForumFilterState> state = EnumSet.noneOf(ForumFilterState.class);

    @Override
    public boolean include(Entry<? extends ForumTableModel, ? extends Integer> entry) {
        if (state.isEmpty()) {
            return true;
        }
        ForumTableModel m = entry.getModel();
        int ind = entry.getIdentifier();

        ForumData fd = m.getValueAt(ind, 0);
        Forum f = fd.getForum();
        ForumStatistic fs = fd.getStat();

        if (state.contains(ForumFilterState.Subscribed) && f != null) {
            if (!f.isSubscribed()) {
                return false;
            }
        }

        if (fs != null) {
            if (state.contains(ForumFilterState.Unread) && fs.getUnreadMessages() == 0){
                return false;
            }

            if (state.contains(ForumFilterState.NotEmpty) && fs.getTotalMessages() <= 0) {
                return false;
            }
        }

        return true;
    }

    public void set(ForumFilterState s, boolean set) {
        if (set) {
            state.add(s);
        } else {
            state.remove(s);
        }
    }

    public boolean is(ForumFilterState s) {
        return state.contains(s);
    }

    public void setState(EnumSet<ForumFilterState> newState) {
        state.clear();
        state.addAll(newState);
    }

    public EnumSet<ForumFilterState> getState() {
        return state.clone();
    }
}
