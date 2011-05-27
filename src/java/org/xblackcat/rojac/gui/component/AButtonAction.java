package org.xblackcat.rojac.gui.component;

import org.xblackcat.rojac.i18n.Messages;

import javax.swing.*;

/**
 * @author xBlackCat
 */

public abstract class AButtonAction extends AbstractAction {
    private final Messages message;
    private final ShortCut shortCut;

    public AButtonAction(Messages message) {
        this.message = message;
        shortCut = null;
    }

    public AButtonAction(ShortCut shortCut) {
        this.message = shortCut.getDescription();
        this.shortCut = shortCut;
    }

    public Messages getMessage() {
        return message;
    }

    public ShortCut getShortCut() {
        return shortCut;
    }
}
