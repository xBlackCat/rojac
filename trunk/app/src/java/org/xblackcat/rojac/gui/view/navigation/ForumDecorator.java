package org.xblackcat.rojac.gui.view.navigation;

import gnu.trove.map.hash.TIntObjectHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.tree.TreeModelSupport;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ForumStatistic;
import org.xblackcat.rojac.gui.view.forumlist.ForumData;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.RojacWorker;

import java.util.*;

/**
 * Helper class to manage forum lists in Navigation view
 * <p/>
 * Date: 18.07.11
 *
 * @author xBlackCat
 */
class ForumDecorator {
    private static final Log log = LogFactory.getLog(ForumDecorator.class);

    private static final Comparator<ANavItem> FORUM_LIST_COMPARATOR = new Comparator<ANavItem>() {
        @Override
        public int compare(ANavItem o1, ANavItem o2) {
            ForumNavItem f1 = (ForumNavItem) o1;
            ForumNavItem f2 = (ForumNavItem) o2;

            if (f1 == null || f1.getForum() == null) {
                return f2 == null || f2.getForum() == null ? 0 : -1;
            } else if (f2 == null || f2.getForum() == null) {
                return 1;
            } else {
                return f1.getForum().getForumName().compareToIgnoreCase(f2.getForum().getForumName());
            }
        }
    };
    private final IStorage storage = ServiceFactory.getInstance().getStorage();

    private final NavModel model;
    private final TreeModelSupport support;

    private final GroupNavItem subscribedForums;
    private final GroupNavItem notSubscribedForums;

    private final TIntObjectHashMap<ForumNavItem> viewedForums = new TIntObjectHashMap<ForumNavItem>();

    public ForumDecorator(NavModel model, TreeModelSupport support) {
        this.model = model;
        this.support = support;

        subscribedForums = new GroupNavItem(Message.View_Navigation_SubscribedForums);
        notSubscribedForums = new GroupNavItem(Message.View_Navigation_NotSubscribedForums);
    }

    ANavItem getNotSubscribedForums() {
        return notSubscribedForums;
    }

    ANavItem getSubscribedForums() {
        return subscribedForums;
    }

    // Groups manipulation
    void loadForums(Collection<ForumData> forums) {
/*
        subscribedForums.children.clear();
        notSubscribedForums.children.clear();

        for (ForumData d : forums) {
            GroupNavItem parent = d.isSubscribed() ? subscribedForums : notSubscribedForums;
            List<ANavItem> children = parent.children;

            if (d.getStat().getTotalMessages() > 0 || d.isSubscribed()) {
                ForumNavItem forum = new ForumNavItem(parent, d);

                children.add(forum);
                viewedForums.put(d.getForumId(), forum);
            }
        }

        Collections.sort(subscribedForums.children, FORUM_LIST_COMPARATOR);
        Collections.sort(notSubscribedForums.children, FORUM_LIST_COMPARATOR);

        support.fireTreeStructureChanged(model.getPathToRoot(subscribedForums));
        support.fireTreeStructureChanged(model.getPathToRoot(notSubscribedForums));
*/
    }

    void updateForum(ForumData d) {
        GroupNavItem parent = d.isSubscribed() ? subscribedForums : notSubscribedForums;

        int forumId = d.getForumId();
        ForumNavItem forum = new ForumNavItem(parent, d);

        if (d.getStat().getTotalMessages() > 0 || d.isSubscribed()) {
            int idx = parent.add(forum, FORUM_LIST_COMPARATOR);
            viewedForums.put(forumId, forum);

            support.fireChildAdded(model.getPathToRoot(parent), idx, forum);
        } else {
            // Remove forum from list if any
            int idx = subscribedForums.indexOf(forum);
            if (idx != -1) {
                ANavItem removed = subscribedForums.remove(idx);
                support.fireChildRemoved(model.getPathToRoot(subscribedForums), idx, removed);
            }
            idx = notSubscribedForums.indexOf(forum);
            if (idx != -1) {
                ANavItem removed = notSubscribedForums.remove(idx);
                support.fireChildRemoved(model.getPathToRoot(notSubscribedForums), idx, removed);
            }
            viewedForums.remove(forumId);
        }
    }

    public void updateSubscribed(int forumId, boolean subscribed) {
        ForumNavItem item = viewedForums.get(forumId);
        if (item == null) {
            // Forum not shown yet - load from DB
            new ForumLoader(forumId).execute();
        } else {
            ForumData fd = new ForumData(
                    item.getForum().setSubscribed(subscribed),
                    item.getStatistic()
            );

            updateForum(fd);
        }
    }

    void loadForumStatistic(int... forumId) {
        new ForumUpdater(forumId).execute();
    }

    public void reloadForums() {
        new ForumLoader().execute();
    }

    private void updateForum(ForumStatistic stat) {
        ForumNavItem navItem = viewedForums.get(stat.getForumId());
        if (navItem != null) {
            navItem.setStatistic(stat);
            support.firePathChanged(model.getPathToRoot(navItem));
        }
    }

    private class ForumUpdater extends RojacWorker<Void, ForumStatistic> {
        private final int[] forumIds;

        public ForumUpdater(int... forumIds) {
            this.forumIds = forumIds;
        }

        @Override
        protected Void perform() throws Exception {
            IForumAH fah = storage.getForumAH();

            Map<Integer, Number> totalMessages = fah.getMessagesInForums(forumIds);
            Map<Integer, Number> unreadMessages = fah.getUnreadMessagesInForums(forumIds);
            Map<Integer, Number> lastPostDate = fah.getLastMessageDateInForums(forumIds);

            for (int forumId : forumIds) {
                Number date = lastPostDate.get(forumId);
                publish(new ForumStatistic(
                        forumId,
                        totalMessages.get(forumId).intValue(),
                        unreadMessages.get(forumId).intValue(),
                        date != null ? date.longValue() : null
                ));
            }

            return null;
        }

        @Override
        protected void process(List<ForumStatistic> chunks) {
            for (ForumStatistic stat : chunks) {
                updateForum(stat);
            }
        }
    }

    private class ForumLoader extends RojacWorker<Void, ForumData> {
        private final Integer forumId;
        private final IForumAH fah = storage.getForumAH();

        ForumLoader(int forumId) {
            this.forumId = forumId;
        }

        private ForumLoader() {
            this.forumId = null;
        }

        @Override
        protected Void perform() throws Exception {
            try {
                Collection<Forum> forums;

                if (forumId == null) {
                    forums = fah.getAllForums();
                } else {
                    forums = Collections.singleton(fah.getForumById(forumId));
                }

                for (Forum f : forums) {
                    int forumId = f.getForumId();

                    Number totalMessages = fah.getMessagesInForum(forumId);
                    Number unreadMessages = fah.getUnreadMessagesInForum(forumId);
                    Number lastPostDate = fah.getLastMessageDateInForum(forumId);
                    publish(
                            new ForumData(
                                    f,
                                    new ForumStatistic(
                                            forumId,
                                            totalMessages.intValue(),
                                            unreadMessages.intValue(),
                                            lastPostDate == null ? null : lastPostDate.longValue()
                                    )
                            )
                    );
                }
            } catch (StorageException e) {
                log.error("Can not load forum list", e);
                throw e;
            }

            return null;
        }

        @Override
        protected void process(List<ForumData> forums) {
            for (ForumData fd : forums) {
                updateForum(fd);
            }
        }
    }
}

