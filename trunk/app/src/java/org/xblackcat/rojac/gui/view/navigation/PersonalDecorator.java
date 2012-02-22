package org.xblackcat.rojac.gui.view.navigation;

import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.data.NewMessage;
import org.xblackcat.rojac.data.ReadStatistic;
import org.xblackcat.rojac.gui.theme.ReadStatusIcon;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IMiscAH;
import org.xblackcat.rojac.service.storage.INewMessageAH;
import org.xblackcat.rojac.service.storage.IStatisticAH;
import org.xblackcat.rojac.service.storage.Storage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

/**
 * @author xBlackCat Date: 22.07.11
 */
class PersonalDecorator extends ADecorator {
    private final GroupItem<PersonalItem> personal = new GroupItem<>(Message.View_Navigation_Item_Personal, ReadStatusIcon.Personal);

    private final PersonalItem outBox = new OutboxItem();
    private final PersonalItem myResponses = new MyResponsesItem();
    private final PersonalItem ignoredTopics = new IgnoredTopicsItem();

    PersonalDecorator(IModelControl modelControl) {
        super(modelControl);

        personal.add(outBox);
        personal.add(myResponses);
        personal.add(ignoredTopics);
    }

    @Override
    AnItem[] getItemsList() {
        return new AnItem[]{
                personal
        };
    }

    public Collection<ILoadTask> reloadInfo(boolean fullReload) {
        Collection<ILoadTask> tasks = new LinkedList<>();
        Integer userId = Property.RSDN_USER_ID.get();
        if (userId != null) {
            tasks.add(new AnswersReloadTask(userId));
        }

        if (fullReload) {
            tasks.add(new OutboxReloadTask());
        }

        return tasks;
    }

    public Collection<OutboxReloadTask> reloadOutbox() {
        return Collections.singleton(new OutboxReloadTask());
    }

    public Collection<IgnoredReloadTask> reloadIgnored() {
        return Collections.singleton(new IgnoredReloadTask());
    }

    public Collection<ILoadTask> alterReadStatus(MessageData post, boolean read) {
        Collection<ILoadTask> tasks = new ArrayList<>(4);
        if (post.getParentUserId() == Property.RSDN_USER_ID.get()) {
            tasks.add(new AdjustResponsesTask(myResponses, read ? -1 : 1));
        }

        return tasks;
    }

    private class AdjustResponsesTask extends AnAdjustUnreadTask<PersonalItem> {
        public AdjustResponsesTask(PersonalItem item, int adjustDelta) {
            super(item, adjustDelta);
        }

        @Override
        public void doSwing(Void data) {
            ReadStatistic  stat = item.getStat();
            int unread = stat.getUnreadMessages() + adjustDelta;
            if (unread < 0) {
                unread = 0;
            } else if (unread > stat.getTotalMessages()) {
                unread = stat.getTotalMessages();
            }

            final ReadStatistic newStat = new ReadStatistic(0, unread, stat.getTotalMessages());
            item.setStat(newStat);

            modelControl.itemUpdated(item);
        }
    }

    private class AnswersReloadTask implements ILoadTask<ReadStatistic> {
        private final int userId;

        private AnswersReloadTask(int userId) {
            this.userId = userId;
        }

        @Override
        public ReadStatistic doBackground() throws Exception {
            IStatisticAH statisticAH = Storage.get(IStatisticAH.class);
            return statisticAH.getUserRepliesStat(userId);
        }

        @Override
        public void doSwing(ReadStatistic data) {
            myResponses.setStat(data);
            modelControl.itemUpdated(myResponses);
        }
    }

    private class OutboxReloadTask implements ILoadTask<ReadStatistic> {
        @Override
        public ReadStatistic doBackground() throws Exception {
            Collection<NewMessage> newMessages = Storage.get(INewMessageAH.class).getAllNewMessages();
            int toSend = 0;
            for (NewMessage nm : newMessages) {
                if (!nm.isDraft()) {
                    ++toSend;
                }
            }

            return new ReadStatistic(0, toSend, newMessages.size());
        }

        @Override
        public void doSwing(ReadStatistic data) {
            if (data != null) {
                outBox.setStat(data);

                modelControl.itemUpdated(outBox);
            }
        }
    }

    private class IgnoredReloadTask implements ILoadTask<Integer> {
        @Override
        public Integer doBackground() throws Exception {
            return Storage.get(IMiscAH.class).getAmountOfIgnoredTopics();
        }

        @Override
        public void doSwing(Integer data) {
            if (data != null) {
                ignoredTopics.setStat(new ReadStatistic(0, 0, data));

                modelControl.itemUpdated(ignoredTopics);
            }
        }
    }
}
