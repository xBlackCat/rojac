package org.xblackcat.rojac.gui.view.navigation;

import gnu.trove.map.hash.TIntObjectHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ForumStatistic;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.gui.theme.ReadStatusIcon;
import org.xblackcat.rojac.gui.view.forumlist.ForumData;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.Storage;
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

        subscribedForums = new GroupItem<>(Message.View_Navigation_Item_SubscribedForums, FORUM_LIST_COMPARATOR, ReadStatusIcon.ForumSubscribed);
        notSubscribedForums = new GroupItem<>(Message.View_Navigation_Item_NotSubscribedForums, FORUM_LIST_COMPARATOR, ReadStatusIcon.Forum);
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

    public ALoadTask updateSubscribed(int forumId, boolean subscribed) {
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

    Collection<ALoadTask> loadForumStatistic(int... forumIds) {
        Collection<ALoadTask> tasks = new ArrayList<>(forumIds.length);

        for (int forumId : forumIds) {
            if (viewedForums.containsKey(forumId)) {
                // Existing forum
                tasks.add(new ForumUpdateTask(forumId));
            } else {
                // Not shown forum
                tasks.add(new ForumLoadTask(forumId));
            }
        }

        return tasks;
    }

    public Collection<ALoadTask> reloadForums() {
        return Collections.singleton((ALoadTask) new ForumReloadTask());
    }

    private void updateForum(ForumStatistic stat) {
        ForumItem navItem = viewedForums.get(stat.getForumId());
        if (navItem != null) {
            navItem.setStatistic(stat);
            modelControl.itemUpdated(navItem);
        }
    }

    public Collection<ALoadTask> alterReadStatus(MessageData messageData, boolean read) {
        ForumItem navItem = viewedForums.get(messageData.getForumId());
        if (navItem != null) {
            Integer ownId = Property.RSDN_USER_ID.get();
            ALoadTask<Void> task = new ForumUnreadAdjustTask(
                    navItem,
                    read ? -1 : 1,
                    messageData.getParentUserId() == ownId && messageData.getUserId() != ownId
            );

            return Collections.singleton((ALoadTask) task);
        }

        return Collections.emptySet();
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

    private class ForumLoadTask extends AForumTask<ForumData> {
        private final int forumId;

        private ForumLoadTask(int forumId) {
            this.forumId = forumId;
        }

        @Override
        public ForumData doBackground() throws Exception {
            try {
                Forum f;

                final IForumAH fah = Storage.get(IForumAH.class);

                f = fah.getForumById(forumId);

                int forumId = f.getForumId();
                ForumStatistic statistic = getForumStatistic(forumId);

                return new ForumData(f, statistic);
            } catch (StorageException e) {
                log.error("Can not load forum list", e);
                throw e;
            }
        }

        @Override
        public void doSwing(ForumData data) {
            updateForum(data.getForum(), data.getStat());
        }
    }

    private class ForumReloadTask extends ALoadTask<Collection<ForumData>> {
        @Override
        public Collection<ForumData> doBackground() throws Exception {
            final IForumAH fah = Storage.get(IForumAH.class);

            try {
                Map<Integer, ForumData> data = new HashMap<>();
                for (Forum f : fah.getAllForums()) {
                    int forumId = f.getForumId();

                    data.put(forumId, new ForumData(f));
                }

                Collection<ForumStatistic> forumsStatistic = fah.getForumsStatistic(Property.RSDN_USER_ID.get(-1));
                for (ForumStatistic stat : forumsStatistic) {
                    data.get(stat.getForumId()).setStat(stat);
                }

                return data.values();
            } catch (StorageException e) {
                log.error("Can not load forum list", e);
                throw e;
            }
        }

        @Override
        public void doSwing(Collection<ForumData> data) {
            // Reload all forums
            modelControl.removeAllChildren(subscribedForums);
            modelControl.removeAllChildren(notSubscribedForums);

            for (ForumData fd : data) {
                updateForum(fd.getForum(), fd.getStat());
            }
        }
    }

    private class ForumUnreadAdjustTask extends AnAdjustUnreadTask<ForumItem> {
        private final boolean reply;

        private ForumUnreadAdjustTask(ForumItem item, int adjustDelta, boolean isReply) {
            super(item, adjustDelta);
            reply = isReply;
        }

        @Override
        public void doSwing(Void data) {
            ForumStatistic statistic = item.getStatistic();
            statistic = new ForumStatistic(
                    statistic.getForumId(),
                    statistic.getTotalMessages(),
                    statistic.getUnreadMessages() + adjustDelta,
                    statistic.getLastMessageDate(),
                    statistic.getUnreadReplies() + (reply ? adjustDelta : 0)
            );
            item.setStatistic(statistic);
            modelControl.itemUpdated(item);
        }
    }
}

