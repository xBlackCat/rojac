package org.xblackcat.rojac.gui.view.navigation;

import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.data.UnreadStatData;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IStatisticAH;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 * @author xBlackCat Date: 22.07.11
 */
class PersonalDecorator extends ADecorator {
    private final GroupItem<PersonalItem> personal = new GroupItem<>(Message.View_Navigation_Item_Personal);

    private final PersonalItem outBox = new OutboxItem();
    private final PersonalItem myResponses = new MyResponsesItem();
    private final PersonalItem drafts = new DraftsItem();

    PersonalDecorator(IModelControl modelControl) {
        super(modelControl);

        personal.add(outBox);
        personal.add(myResponses);
    }

    @Override
    AnItem[] getItemsList() {
        return new AnItem[]{
                personal
        };
    }

    public Collection<ALoadTask> reloadInfo(boolean fullReload) {
        Collection<ALoadTask> tasks = new LinkedList<>();
        Integer userId = Property.RSDN_USER_ID.get();
        if (userId != null) {
            tasks.add(new AnswersReloadTask(userId));
        }

        if (fullReload) {
            // Load others values
        }

        return tasks;
    }

    public Collection<ALoadTask> alterReadStatus(MessageData post, boolean read) {
        Collection<ALoadTask> tasks = new ArrayList<>(4);
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
        void doSwing(Void data) {
            UnreadStatData stat = item.getStat();
            final UnreadStatData newStat = new UnreadStatData(stat.getUnread() + adjustDelta, stat.getTotal());
            item.setStat(newStat);

            modelControl.itemUpdated(item);
        }
    }

    private class AnswersReloadTask extends ALoadTask<UnreadStatData> {
        private final int userId;
        protected IStatisticAH statisticAH;

        private AnswersReloadTask(int userId) {
            this.userId = userId;
            statisticAH = ServiceFactory.getInstance().getStorage().getStatisticAH();
        }

        @Override
        UnreadStatData doBackground() throws Exception {
            return statisticAH.getUserRepliesStat(userId);
        }

        @Override
        void doSwing(UnreadStatData data) {
            myResponses.setStat(data);
            modelControl.itemUpdated(myResponses);
        }
    }
}
