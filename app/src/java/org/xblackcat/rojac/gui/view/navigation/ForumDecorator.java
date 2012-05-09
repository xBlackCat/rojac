package org.xblackcat.rojac.gui.view.navigation;

import gnu.trove.impl.sync.TSynchronizedIntObjectMap;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.DiscussionStatistic;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ItemStatisticData;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.gui.theme.ReadStatusIcon;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.IResult;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.RojacUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

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

    private final TIntObjectMap<ForumItem> viewedForums = new TIntObjectHashMap<>();

    private final TIntObjectMap<Forum> forumsCache = new TSynchronizedIntObjectMap<>(new TIntObjectHashMap<Forum>());

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

    void updateForum(int forumId, DiscussionStatistic statistic) {
        Forum forum = forumsCache.get(forumId);

        if (forum == null) {
            assert false : "Unknown forum id: " + forumId;
            return;
        }

        boolean subscribed = forum.isSubscribed();
        AGroupItem<ForumItem> parent = subscribed ? subscribedForums : notSubscribedForums;

        ForumItem forumItem = new ForumItem(parent, forum, statistic);

        // Remove forum from list if any
        modelControl.safeRemoveChild(subscribedForums, forumItem);
        modelControl.safeRemoveChild(notSubscribedForums, forumItem);

        if (statistic == null || statistic.getTotalMessages() > 0 || subscribed) {
            modelControl.addChild(parent, forumItem);
            viewedForums.put(forumId, forumItem);
        } else {
            viewedForums.remove(forumId);
        }

        modelControl.itemUpdated(subscribedForums);
        modelControl.itemUpdated(notSubscribedForums);
    }

    public ILoadTask updateSubscribed(int forumId, boolean subscribed) {
        Forum forum = forumsCache.get(forumId);

        if (forum == null) {
            // Forum not shown yet - load from DB
            return new ForumLoadTask(forumId);
        } else {
            forum = forum.setSubscribed(subscribed);
            forumsCache.put(forum.getForumId(), forum);

            ForumItem item = viewedForums.get(forumId);

            if (item != null) {
                DiscussionStatistic statistic = item.getStatistic();

                updateForum(forumId, statistic);
                return null;
            } else {
                return new ForumUpdateTask(forumId);
            }
        }
    }

    Collection<ILoadTask> loadForumStatistic(int... forumIds) {
        Collection<ILoadTask> tasks = new ArrayList<>(forumIds.length);

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

    public Collection<ForumReloadTask> reloadForums() {
        return Collections.singleton(new ForumReloadTask());
    }

    public Collection<ILoadTask<Void>> alterReadStatus(MessageData messageData, boolean read) {
        ForumItem navItem = viewedForums.get(messageData.getForumId());
        if (navItem != null) {
            Integer ownId = Property.RSDN_USER_ID.get();
            ILoadTask<Void> task = new ForumUnreadAdjustTask(
                    navItem,
                    read ? -1 : 1,
                    messageData.getParentUserId() == ownId && messageData.getUserId() != ownId
            );

            return Collections.singleton(task);
        }

        return Collections.emptySet();
    }


    private class ForumUpdateTask implements ILoadTask<DiscussionStatistic> {
        protected final int forumId;

        protected ForumUpdateTask(int forumId) {
            this.forumId = forumId;
        }

        @Override
        public DiscussionStatistic doBackground() throws Exception {
            assert RojacUtils.checkThread(false);

            final IForumAH fah = Storage.get(IForumAH.class);

            DiscussionStatistic forumStatistic = fah.getForumStatistic(forumId, Property.RSDN_USER_ID.get());

            if (forumStatistic == null) {
                forumStatistic = DiscussionStatistic.NO_STAT;
            }

            return forumStatistic;
        }

        @Override
        public void doSwing(DiscussionStatistic data) {
            updateForum(forumId, data);
        }
    }

    private class ForumLoadTask extends ForumUpdateTask {
        protected ForumLoadTask(int forumId) {
            super(forumId);
        }

        @Override
        public DiscussionStatistic doBackground() throws Exception {
            try {
                Forum f;

                final IForumAH fah = Storage.get(IForumAH.class);

                f = fah.getForumById(forumId);

                if (f == null) {
                    assert false : "Unknown forum id: " + forumId;
                    return null;
                }


                forumsCache.put(forumId, f);

                return super.doBackground();
            } catch (StorageException e) {
                log.error("Can not load forum list", e);
                throw e;
            }
        }
    }

    private class ForumReloadTask implements ILoadTask<Collection<ItemStatisticData<Forum>>> {
        @Override
        public Collection<ItemStatisticData<Forum>> doBackground() throws Exception {
            final IForumAH fah = Storage.get(IForumAH.class);

            Collection<ItemStatisticData<Forum>> forums = new ArrayList<>();
            
            try (IResult<ItemStatisticData<Forum>> allForums = fah.getAllForums()) {
                CollectionUtils.addAll(forums, allForums.iterator());
            }

            return forums;
        }

        @Override
        public void doSwing(Collection<ItemStatisticData<Forum>> data) {
            Collection<ForumUpdateTask> updateTasks = new ArrayList<>();

            // Reload all forums
            modelControl.removeAllChildren(subscribedForums);
            modelControl.removeAllChildren(notSubscribedForums);
            viewedForums.clear();

            for (ItemStatisticData<Forum> fd : data) {
                Forum forum = fd.getItem();
                forumsCache.put(forum.getForumId(), forum);

                boolean subscribed = forum.isSubscribed();
                AGroupItem<ForumItem> parent = subscribed ? subscribedForums : notSubscribedForums;

                int forumId = forum.getForumId();
                DiscussionStatistic statistic = fd.getItemReadStatistic();

                ForumItem forumItem = new ForumItem(parent, forum, statistic);

                if (statistic == null || statistic.getTotalMessages() > 0 || subscribed) {
                    modelControl.addChild(parent, forumItem);
                    viewedForums.put(forumId, forumItem);
                    updateTasks.add(new ForumUpdateTask(forumId));
                } else {
                    viewedForums.remove(forumId);
                }
            }

            modelControl.itemUpdated(subscribedForums);
            modelControl.itemUpdated(notSubscribedForums);

            new LoadTaskExecutor(updateTasks).execute();
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
            DiscussionStatistic statistic = item.getStatistic();
            if (statistic == null) {
                return;
            }

            statistic = new DiscussionStatistic(
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

