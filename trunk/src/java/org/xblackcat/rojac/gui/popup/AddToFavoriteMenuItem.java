package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.gui.view.model.FavoriteType;

import javax.swing.*;

/**
 * @author xBlackCat
 */

class AddToFavoriteMenuItem extends JMenuItem {
    public AddToFavoriteMenuItem(String text, final FavoriteType type, final int itemId) {
        super(text);

        addActionListener(e -> new FavoriteAdder(type, itemId).execute());
    }
}
