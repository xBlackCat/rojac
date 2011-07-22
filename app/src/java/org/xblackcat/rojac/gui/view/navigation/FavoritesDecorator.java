package org.xblackcat.rojac.gui.view.navigation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.i18n.Message;

import java.util.Comparator;

/**
 * Helper class to work only with favorites in navigation model
 *
 * @author xBlackCat Date: 22.07.11
 */
class FavoritesDecorator extends ADecorator {
    private static final Log log = LogFactory.getLog(FavoritesDecorator.class);

    private static final Comparator<AnItem> FAVORITES_LIST_COMPARATOR = new Comparator<AnItem>() {
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
