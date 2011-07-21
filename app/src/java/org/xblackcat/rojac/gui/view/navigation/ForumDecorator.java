package org.xblackcat.rojac.gui.view.navigation;

import gnu.trove.map.hash.TIntObjectHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

    private static final Comparator<AnItem> FORUM_LIST_COMPARATOR = new Comparator<AnItem>() {
        @Override
        public int compare(AnItem o1, AnItem o2) {
            ForumItem f1 = (ForumItem) o1;
            ForumItem f2 = (ForumItem) o2;

            if (f1 == null || f1.getForum() == null) {
                return f2 == null || f2.getForum() == null ? 0 : -1;
            } else if (f2 == null || f2.getForum() == null) {
                return 1;
            } else {
                return f1.getForum().getForumName().compareToIgnoreCase(f2.getForum().getForumName());
            }
        }
    };

    private final AModelControl modelControl;

    private final AGroupItem subscribedForums;
    private final AGroupItem notSubscribedForums;

    private final TIntObjectHashMap<ForumItem> viewedForums = new TIntObjectHashMap<ForumItem>();

    public ForumDecorator(AModelControl modelControl) {
        this.modelControl = modelControl;

        subscribedForums = new GroupItem(Message.View_Navigation_Item_SubscribedForums, FORUM_LIST_COMPARATOR);
        notSubscribedForums = new GroupItem(Message.View_Navigation_Item_NotSubscribedForums, FORUM_LIST_COMPARATOR);
    }

    AnItem getNotSubscribedForums() {
        return notSubscribedForums;
    }

    AnItem getSubscribedForums() {
        return subscribedForums;
    }

    void updateForum(ForumData d) {
        boolean subscribed = d.getForum().isSubscribed();
        AGroupItem parent = subscribed ? subscribedForums : notSubscribedForums;

        int forumId = d.getForumId();
        ForumItem forum = new ForumItem(parent, d);

        // Remove forum from list if any
        modelControl.safeRemoveChild(subscribedForums, forum);
        modelControl.safeRemoveChild(notSubscribedForums, forum);

        if (d.getStat().getTotalMessages() > 0 || subscribed) {
            modelControl.addChild(parent, forum);
            viewedForums.put(forumId, forum);
        } else {
            viewedForums.remove(forumId);
        }
    }

    public void updateSubscribed(int forumId, boolean subscribed) {
        ForumItem item = viewedForums.get(forumId);
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
        ForumItem navItem = viewedForums.get(stat.getForumId());
        if (navItem != null) {
            navItem.setStatistic(stat);
            modelControl.itemUpdated(navItem);
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

