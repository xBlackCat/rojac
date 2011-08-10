package org.xblackcat.rojac.gui.view.navigation;

import org.xblackcat.rojac.i18n.Message;

/**
 * @author xBlackCat Date: 22.07.11
 */
class PersonalDecorator extends ADecorator {
    private final GroupItem personal = new GroupItem(Message.View_Navigation_Item_Personal);

    private final PersonalItem outBox = new OutboxItem();
    private final PersonalItem myResponses = new MyResponsesItem();
    private final PersonalItem drafts = new DraftsItem();

    PersonalDecorator(AModelControl modelControl) {
        super(modelControl);

        personal.add(outBox);
    }

    @Override
    AnItem[] getItemsList() {
        return new AnItem[]{
                personal
        };
    }
}
