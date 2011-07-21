package org.xblackcat.rojac.gui.view.navigation;

import gnu.trove.map.hash.TIntObjectHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.tree.TreeModelSupport;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ForumStatistic;
import org.xblackcat.rojac.gui.view.forumlist.ForumData;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.storage.StorageException;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

    private final NavModel model;
    private final TreeModelSupport support;

    private final GroupNavItem subscribedForums;
    private final GroupNavItem notSubscribedForums;

    private final TIntObjectHashMap<ForumNavItem> viewedForums = new TIntObjectHashMap<ForumNavItem>();

    public ForumDecorator(NavModel model, TreeModelSupport support) {
        this.model = model;
        this.support = support;

        subscribedForums = new GroupNavItem(Message.View_Navigation_Item_SubscribedForums);
        notSubscribedForums = new GroupNavItem(Message.View_Navigation_Item_NotSubscribedForums);
    }

    ANavItem getNotSubscribedForums() {
        return notSubscribedForums;
    }

    ANavItem getSubscribedForums() {
        return subscribedForums;
    }

    void updateForum(ForumData d) {
        boolean subscribed = d.getForum().isSubscribed();
        GroupNavItem parent = subscribed ? subscribedForums : notSubscribedForums;

        int forumId = d.getForumId();
        ForumNavItem forum = new ForumNavItem(parent, d);

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

        if (d.getStat().getTotalMessages() > 0 || subscribed) {
            idx = parent.add(forum, FORUM_LIST_COMPARATOR);
            viewedForums.put(forumId, forum);

            support.fireChildAdded(model.getPathToRoot(parent), idx, forum);
        } else {
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

    private class ForumUpdater extends AForumUpdater<Void, ForumStatistic> {
        private final int[] forumIds;

        public ForumUpdater(int... forumIds) {
            this.forumIds = forumIds;
        }

        @Override
        protected Void perform() throws Exception {
            for (int forumId : forumIds) {
                publish(getForumStatistic(forumId));
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

    private class ForumLoader extends AForumUpdater<Void, ForumData> {
        private final Integer forumId;

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

                    publish(
                            new ForumData(
                                    f,
                                    getForumStatistic(forumId)
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

