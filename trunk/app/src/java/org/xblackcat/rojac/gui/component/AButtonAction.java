package org.xblackcat.rojac.gui.component;

import org.xblackcat.rojac.i18n.Message;

import javax.swing.*;

/**
 * @author xBlackCat
 */

public abstract class AButtonAction extends AbstractAction {
    private final Message message;
    private final ShortCut shortCut;

    public AButtonAction(Message message) {
        this.message = message;
        shortCut = null;
    }

    public AButtonAction(ShortCut shortCut) {
        this.message = shortCut.getDescription();
        this.shortCut = shortCut;
    }

    public Message getMessage() {
        return message;
    }

    public ShortCut getShortCut() {
        return shortCut;
    }
}
