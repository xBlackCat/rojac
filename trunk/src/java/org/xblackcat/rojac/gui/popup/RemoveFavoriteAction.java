package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.i18n.Message;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author xBlackCat
 */
class RemoveFavoriteAction extends AbstractAction {
    private final int favoriteId;

    public RemoveFavoriteAction(int favoriteId) {
        super(Message.Popup_View_Remove.get());
        this.favoriteId = favoriteId;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new FavoriteRemover(favoriteId).execute();
    }
}
