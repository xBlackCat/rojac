package org.xblackcat.rojac.gui.view.navigation;

import gnu.trove.map.hash.TIntObjectHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ForumStatistic;
import org.xblackcat.rojac.gui.theme.ReadStatusIcon;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.storage.StorageException;

import java.util.*;

/**
 * Helper class to manage forum lists in Navigation view
 * <p/>
 * Date: 18.07.11
 *
 * @author xBlackCat
 */
class ForumDecorator extends ADecorator {
    private static final Log log = LogFactory.getLog(ForumDecorator.class);

    private static final Comparator<ForumItem> FORUM_LIST_COMPARATOR = new Comparator<ForumItem>() {
        @Override
        public int compare(ForumItem o1, ForumItem o2) {
            if (o1 == null || o1.getForum() == null) {
                return o2 == null || o2.getForum() == null ? 0 : -1;
            } else if (o2 == null || o2.getForum() == null) {
                return 1;
            } else {
                return o1.getForum().getForumName().compareToIgnoreCase(o2.getForum().getForumName());
            }
        }
    };

    private final AGroupItem<ForumItem> subscribedForums;
    private final AGroupItem<ForumItem> notSubscribedForums;

    private final TIntObjectHashMap<ForumItem> viewedForums = new TIntObjectHashMap<>();

    public ForumDecorator(IModelControl modelControl) {
        super(modelControl);

        subscribedForums = new GroupItem<>(Message.View_Navigation_Item_SubscribedForums, FORUM_LIST_COMPARATOR, ReadStatusIcon.Thread);
        notSubscribedForums = new GroupItem<>(Message.View_Navigation_Item_NotSubscribedForums, FORUM_LIST_COMPARATOR, ReadStatusIcon.Thread);
    }

    @Override
    AnItem[] getItemsList() {
        return new AnItem[]{
                subscribedForums,
                notSubscribedForums
        };
    }

    void updateForum(Forum forum, ForumStatistic statistic) {
        boolean subscribed = forum.isSubscribed();
        AGroupItem<ForumItem> parent = subscribed ? subscribedForums : notSubscribedForums;

        int forumId = forum.getForumId();
        ForumItem forumItem = new ForumItem(parent, forum, statistic);

        // Remove forum from list if any
        modelControl.safeRemoveChild(subscribedForums, forumItem);
        modelControl.safeRemoveChild(notSubscribedForums, forumItem);

        if (statistic.getTotalMessages() > 0 || subscribed) {
            modelControl.addChild(parent, forumItem);
            viewedForums.put(forumId, forumItem);
        } else {
            viewedForums.remove(forumId);
        }
    }

    public ILoadTask updateSubscribed(int forumId, boolean subscribed) {
        ForumItem item = viewedForums.get(forumId);
        if (item == null) {
            // Forum not shown yet - load from DB
            return new ForumLoadTask(forumId);
        } else {
            Forum forum = item.getForum().setSubscribed(subscribed);
            ForumStatistic statistic = item.getStatistic();

            updateForum(forum, statistic);
            return null;
        }
    }

    ILoadTask[] loadForumStatistic(int... forumIds) {
        ILoadTask[] tasks = new ILoadTask[forumIds.length];

        for (int i = 0, l = tasks.length; i < l; i++) {
            tasks[i] = new ForumUpdateTask(forumIds[i]);
        }

        return tasks;
    }

    public ILoadTask reloadForums() {
        return new ForumLoadTask();
    }

    private void updateForum(ForumStatistic stat) {
        ForumItem navItem = viewedForums.get(stat.getForumId());
        if (navItem != null) {
            navItem.setStatistic(stat);
            modelControl.itemUpdated(navItem);
        }
    }

    public ILoadTask[] alterReadStatus(int forumId, boolean read) {
        ForumItem navItem = viewedForums.get(forumId);
        if (navItem != null) {
            ILoadTask<Void> task = new ForumUnreadAdjustTask(navItem, read ? -1 : 1);

            return ALoadTask.group(task);
        }

        return ILoadTask.NO_TASKS;
    }


    private class ForumUpdateTask extends AForumTask<ForumStatistic> {
        private final int forumId;

        protected ForumUpdateTask(int forumId) {
            this.forumId = forumId;
        }

        @Override
        public ForumStatistic doBackground() throws Exception {
            return getForumStatistic(forumId);
        }

        @Override
        public void doSwing(ForumStatistic data) {
            updateForum(data);
        }
    }

    private class ForumLoadTask extends AForumTask<Map<Forum, ForumStatistic>> {
        private final Integer forumId;

        private ForumLoadTask() {
            forumId = null;
        }

        private ForumLoadTask(int forumId) {
            this.forumId = forumId;
        }

        @Override
        public Map<Forum, ForumStatistic> doBackground() throws Exception {
            Map<Forum, ForumStatistic> result = new HashMap<>();
            try {
                Collection<Forum> forums;

                if (forumId == null) {
                    forums = fah.getAllForums();
                } else {
                    forums = Collections.singleton(fah.getForumById(forumId));
                }

                for (Forum f : forums) {
                    int forumId = f.getForumId();
                    ForumStatistic statistic = getForumStatistic(forumId);

                    result.put(f, statistic);
                }
            } catch (StorageException e) {
                log.error("Can not load forum list", e);
                throw e;
            }

            return result;
        }

        @Override
        public void doSwing(Map<Forum, ForumStatistic> data) {
            for (Map.Entry<Forum, ForumStatistic> fd : data.entrySet()) {
                updateForum(fd.getKey(), fd.getValue());
            }
        }
    }

    private class ForumUnreadAdjustTask extends AnAdjustUnreadTask<ForumItem> {
        private ForumUnreadAdjustTask(ForumItem item, int adjustDelta) {
            super(item, adjustDelta);
        }

        @Override
        public void doSwing(Void data) {
            ForumStatistic statistic = item.getStatistic();
            statistic = new ForumStatistic(
                    statistic.getForumId(),
                    statistic.getTotalMessages(),
                    statistic.getUnreadMessages() + adjustDelta,
                    statistic.getLastMessageDate(),
                    statistic.getUnreadReplies()
            );
            item.setStatistic(statistic);
            modelControl.itemUpdated(item);
        }
    }
}

