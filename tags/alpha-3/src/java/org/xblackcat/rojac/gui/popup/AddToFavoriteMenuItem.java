package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.gui.view.model.FavoriteType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author xBlackCat
 */

class AddToFavoriteMenuItem extends JMenuItem {
    public AddToFavoriteMenuItem(String text, final FavoriteType type, final int itemId) {
        super(text);

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FavoriteAdder(type, itemId).execute();
            }
        });
    }
}
