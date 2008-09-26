package org.xblackcat.rojac.gui.model;

import gnu.trove.TIntArrayList;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ForumStatistic;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.executor.IExecutor;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.StorageException;

import javax.swing.*;
import java.util.*;

/**
 * Date: 12 лип 2008
 *
 * @author xBlackCat
 */

public class ForumListModel extends AbstractListModel {
    private static final Log log = LogFactory.getLog(ForumListModel.class);

    private List<ForumData> forums = new ArrayList<ForumData>();
    private List<ForumData> subcribedForums = new ArrayList<ForumData>();
    private List<ForumData> filledForums = new ArrayList<ForumData>();

    private ForumInfoProcessor processor = new ForumInfoProcessor();

    private ForumViewMode mode = ForumViewMode.SHOW_ALL;
    private static final Comparator<ForumData> FORUM_COMPARATOR = new Comparator<ForumData>() {
        public int compare(ForumData o1, ForumData o2) {
            Forum f1 = o1.getForum();
            Forum f2 = o2.getForum();

            if (f1 == null) {
                return f2 == null ? 0 : 1;
            } else {
                return f2 == null ? -1 : f1.getForumName().compareToIgnoreCase(f2.getForumName());
            }
        }
    };

    private List<ForumData> getList() {
        switch (mode) {
            case SHOW_ALL:
                return forums;
            case SHOW_NOT_EMPTY:
                return filledForums;
            case SHOW_SUBCRIBED:
                return subcribedForums;
        }

        throw new RuntimeException("Somehow we got invalid mode.");
    }

    public int getSize() {
        return getList().size();
    }

    public ForumData getElementAt(int index) {
        return getList().get(index);
    }

    public ForumViewMode getMode() {
        return mode;
    }

    private ForumData getForumData(int id) {
        for (ForumData fd : forums) {
            if (fd.getForumId() == id) {
                return fd;
            }
        }
        return null;
    }

    public void setMode(ForumViewMode mode) {
        fireIntervalRemoved(this, 0, getSize());
        this.mode = mode;
        fireIntervalAdded(this, 0, getSize());
    }

    public void setForums(Forum[] forums) {
        int last = getSize();
        this.forums = new ArrayList<ForumData>();
        subcribedForums = new ArrayList<ForumData>();

        TIntArrayList ids = new TIntArrayList();

        for (Forum f : forums) {
            ForumData fd = new ForumData(f);
            if (f.isSubscribed()) {
                subcribedForums.add(fd);
            }
            this.forums.add(fd);
            ids.add(f.getForumId());
        }

        Collections.sort(subcribedForums, FORUM_COMPARATOR);
        Collections.sort(this.forums, FORUM_COMPARATOR);

        fireIntervalRemoved(this, 0, last);
        fireIntervalAdded(this, 0, getSize());

        processor.processForums(ids.toNativeArray());
    }

    public void reloadInfo(int forumId) {
        processor.processForums(forumId);
    }

    private class ForumInfoProcessor {
        private final Queue<Integer> forumIds = new LinkedList<Integer>();

        private final Runnable processor = new Runnable() {
            public void run() {
                IStorage storage = ServiceFactory.getInstance().getStorage();
                IForumAH fah = storage.getForumAH();

                boolean process;
                do {
                    final int id;
                    synchronized (forumIds) {
                        id = forumIds.remove();
                    }

                    int total = 0;
                    int unread = 0;
                    Forum f;
                    try {
                        f = fah.getForumById(id);
                    } catch (StorageException e) {
                        log.error("Can not load forum information for forum [id:" + id + "].", e);
                        f = null;
                    }

                    try {
                        total = fah.getMessagesInForum(id);
                        unread = fah.getUnreadMessagesInForum(id);
                    } catch (StorageException e) {
                        log.error("Can not load statistic for forum [id:" + id + "].", e);
                    }

                    final ForumStatistic stat = new ForumStatistic(total, unread);
                    final Forum forum = f;

                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            ForumData data = getForumData(id);

                            boolean wasSubscribed = data.getForum() != null && data.getForum().isSubscribed();

                            if (forum != null) {
                                data.setForum(forum);
                            }

                            data.setStat(stat);
                            if (stat.getTotalMessages() > 0) {
                                filledForums.add(data);
                                Collections.sort(filledForums, FORUM_COMPARATOR);
                            }

                            if (wasSubscribed != forum.isSubscribed()) {
                                if (wasSubscribed) {
                                    int ind = subcribedForums.indexOf(data);
                                    subcribedForums.remove(ind);

                                    if (mode == ForumViewMode.SHOW_SUBCRIBED) {
                                        fireIntervalRemoved(ForumListModel.this, ind, ind);
                                    }
                                } else {
                                    subcribedForums.add(data);
                                    Collections.sort(filledForums, FORUM_COMPARATOR);

                                    // The next lines were added for future purposes. I hope.
                                    // Really, the condition in the `if` statment will be always `false` 
                                    int ind = subcribedForums.indexOf(data);

                                    if (mode == ForumViewMode.SHOW_SUBCRIBED) {
                                        fireIntervalAdded(ForumListModel.this, ind, ind);
                                    }
                                }
                            }

                            int ind = getList().indexOf(data);
                            if (mode == ForumViewMode.SHOW_NOT_EMPTY) {
                                fireIntervalAdded(ForumListModel.this, ind, ind);
                            } else {
                                fireContentsChanged(ForumListModel.this, ind, ind);
                            }
                        }
                    });

                    synchronized (forumIds) {
                        process = !forumIds.isEmpty();
                    }
                } while (process);
            }
        };

        public void processForums(int... id) {
            boolean startTask;
            synchronized (forumIds) {
                startTask = forumIds.isEmpty();
                forumIds.addAll(Arrays.asList(ArrayUtils.toObject(id)));
            }
            if (startTask) {
                IExecutor executor = ServiceFactory.getInstance().getExecutor();
                executor.execute(processor);
            }
        }
    }
}
