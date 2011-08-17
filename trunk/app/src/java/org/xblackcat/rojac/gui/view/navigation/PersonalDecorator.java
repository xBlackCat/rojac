package org.xblackcat.rojac.gui.view.navigation;

import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IStatisticAH;

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
    }

    @Override
    AnItem[] getItemsList() {
        return new AnItem[]{
                personal
        };
    }

    public ALoadTask[] reloadInfo() {
        Integer userId = Property.RSDN_USER_ID.get();
        if (userId == null) {
            return ALoadTask.NO_TASKS;
        }

        return ALoadTask.NO_TASKS;
    }

    private class AnswersReloadTask extends ALoadTask<Integer> {
        private final int userId;
        protected IStatisticAH statisticAH;

        private AnswersReloadTask(int userId) {
            this.userId = userId;
            statisticAH = ServiceFactory.getInstance().getStorage().getStatisticAH();
        }

        @Override
        Integer doBackground() throws Exception {


            return null;
        }

        @Override
        void doSwing(Integer data) {
        }
    }
}
