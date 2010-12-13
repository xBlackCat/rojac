package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.view.ViewType;
import org.xblackcat.rojac.i18n.Messages;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
* @author xBlackCat
*/
class OpenFavoriteAction extends AbstractAction {
    private final IAppControl appControl;
    private final int favoriteId;

    public OpenFavoriteAction(IAppControl appControl, int favoriteId) {
        super(Messages.Popup_View_Forums_Open.get());
        this.appControl = appControl;
        this.favoriteId = favoriteId;
    }

    public void actionPerformed(ActionEvent e) {
        appControl.openTab(ViewType.Favorite.makeId(favoriteId));
    }
}
