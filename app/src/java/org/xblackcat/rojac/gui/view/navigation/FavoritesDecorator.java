package org.xblackcat.rojac.gui.view.navigation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.i18n.Message;

/**
 * Helper class to work only with favorites in navigation model
 *
 * @author xBlackCat Date: 22.07.11
 */
class FavoritesDecorator extends ADecorator {
    private static final Log log = LogFactory.getLog(FavoritesDecorator.class);

    private final AGroupItem favorites;

    public FavoritesDecorator(AModelControl modelControl) {
        super(modelControl);
        favorites = new GroupItem(Message.View_Navigation_Item_Favorites);
    }

    @Override
    AnItem[] getItemsList() {
        return new AnItem[]{
                favorites
        };
    }
}
