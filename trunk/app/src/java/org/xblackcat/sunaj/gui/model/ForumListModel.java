package org.xblackcat.sunaj.gui.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.sunaj.data.Forum;
import org.xblackcat.sunaj.service.ServiceFactory;
import org.xblackcat.sunaj.service.storage.IForumAH;
import org.xblackcat.sunaj.service.storage.StorageException;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 12 лип 2008
 *
 * @author xBlackCat
 */

public class ForumListModel extends AbstractListModel {
    private static final Log log = LogFactory.getLog(ForumListModel.class);

    private Forum[] forums = new Forum[0];
    private Forum[] subcribedForums;
    private Forum[] filledForums;

    private ForumViewMode mode = ForumViewMode.SHOW_ALL;

    public int getSize() {
        switch (mode) {
            case SHOW_ALL:
                return forums.length;
            case SHOW_NOT_EMPTY:
                return filledForums.length;
            case SHOW_SUBCRIBED:
                return subcribedForums.length;
        }

        throw new RuntimeException("Somehow we got invalid mode.");
    }

    public Forum getElementAt(int index) {
        switch (mode) {
            case SHOW_ALL:
                return forums[index];
            case SHOW_NOT_EMPTY:
                return filledForums[index];
            case SHOW_SUBCRIBED:
                return subcribedForums[index];
        }

        throw new RuntimeException("Somehow we got invalid mode.");
    }

    public ForumViewMode getMode() {
        return mode;
    }

    public void setMode(ForumViewMode mode) {
        if (mode == ForumViewMode.SHOW_NOT_EMPTY && filledForums == null) {
            // Load forums information from database
            List<Forum> ff = new ArrayList<Forum>();

            IForumAH fah = ServiceFactory.getInstance().getStorage().getForumAH();

            try {
                for (Forum f : forums) {
                    if (fah.getMessagesInForum(f.getForumId()) > 0) {
                        ff.add(f);
                    }
                }
            } catch (StorageException e) {
                log.error("Can not obtain number of messages in forum.", e);
            }

            filledForums = ff.toArray(new Forum[ff.size()]);
        }

        fireIntervalRemoved(this, 0, getSize());
        this.mode = mode;
        fireIntervalAdded(this, 0, getSize());
    }

    public void setForums(Forum[] forums) {
        this.forums = forums;
        filledForums = null;
        List<Forum> sf = new ArrayList<Forum>();
        for (Forum f : forums) {
            if (f.isSubscribed()) {
                sf.add(f);
            }
        }
        subcribedForums = sf.toArray(new Forum[sf.size()]);

        setMode(getMode());
    }

    public void updateSubscribed() {
        List<Forum> sf = new ArrayList<Forum>();
        for (Forum f : forums) {
            if (f.isSubscribed()) {
                sf.add(f);
            }
        }
        subcribedForums = sf.toArray(new Forum[sf.size()]);

        setMode(getMode());
    }
}
