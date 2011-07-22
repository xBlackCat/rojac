package org.xblackcat.rojac.gui.view.navigation;

import org.xblackcat.rojac.i18n.Message;

/**
 * @author xBlackCat Date: 22.07.11
 */
public class PersonalDecorator extends ADecorator {
    private final GroupItem personal;

    PersonalDecorator(AModelControl modelControl) {
        super(modelControl);
        personal = new GroupItem(Message.View_Navigation_Item_Personal);
    }

    @Override
    AnItem[] getItemsList() {
        return new AnItem[]{
                personal
        };
    }
}
